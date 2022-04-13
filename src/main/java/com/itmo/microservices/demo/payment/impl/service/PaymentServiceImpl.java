package com.itmo.microservices.demo.payment.impl.service;

import com.itmo.microservices.demo.external.ExternalSystemService;
import com.itmo.microservices.demo.external.TransactionResponse;
import com.itmo.microservices.demo.external.TransactionStatus;
import com.itmo.microservices.demo.lib.common.order.dto.OrderStatusEnum;
import com.itmo.microservices.demo.lib.common.order.entity.OrderEntity;
import com.itmo.microservices.demo.lib.common.order.repository.OrderItemRepository;
import com.itmo.microservices.demo.lib.common.order.repository.OrderRepository;
import com.itmo.microservices.demo.order.api.service.OrderService;
import com.itmo.microservices.demo.payment.PaymentServiceConstants;
import com.itmo.microservices.demo.payment.api.model.FinancialOperationType;
import com.itmo.microservices.demo.payment.api.model.PaymentStatus;
import com.itmo.microservices.demo.payment.api.model.PaymentSubmissionDto;
import com.itmo.microservices.demo.payment.api.model.UserAccountFinancialLogRecordDto;
import com.itmo.microservices.demo.payment.api.service.PaymentService;
import com.itmo.microservices.demo.payment.impl.model.PaymentLogRecordEntity;
import com.itmo.microservices.demo.payment.impl.model.UserAccountFinancialLogRecord;
import com.itmo.microservices.demo.payment.impl.repository.PaymentLogRecordRepository;
import com.itmo.microservices.demo.payment.impl.repository.UserAccountFinancialLogRecordRepository;
import com.itmo.microservices.demo.payment.utils.UserAccountFinancialLogRecordUtils;
import com.itmo.microservices.demo.users.api.exception.UserNotFoundException;
import com.itmo.microservices.demo.users.api.service.UserService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final UserAccountFinancialLogRecordRepository userAccountFinancialLogRecordRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final PaymentLogRecordRepository paymentLogRecordRepository;
    private final OrderService orderService;
    private final UserService userService;
    private final MeterRegistry meterRegistry;
    private final ExternalSystemService externalSystemService;
    @Value("#{environment['service.name']}")
    private String serviceName = "";

    @Override
    public List<UserAccountFinancialLogRecordDto> getFinlog(UUID orderId) {
        var list = orderId != null ?
                userAccountFinancialLogRecordRepository.findAllByOrderId(orderId) :
                userAccountFinancialLogRecordRepository.findAll();
        log.info("Found [{}] items for order id [{}]", list.size(), orderId);
        return list
                .stream()
                .map(UserAccountFinancialLogRecordUtils::entityToDto)
                .collect(Collectors.toList());
    }

    //static final Counter revenue =
            //Counter.build().name("revenue_total").help("Total revenue").labelNames("serviceName").register();
//    private Counter.Builder revenue = Counter.builder("revenue_total").description("Total revenue");

    @Override
    public PaymentSubmissionDto executeOrderPayment(UserDetails user, UUID orderId) {
        Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(orderId);
        if (optionalOrderEntity.isEmpty()) {
            log.info("Order with id [{}] was not found", orderId);
            return null;
        }
        OrderEntity orderEntity = optionalOrderEntity.get();
        var itemsMap = orderEntity.getItemsMap();
//        Counter counter = revenue.tags("serviceName", "p03").register(meterRegistry);
        if (itemsMap == null) {
            log.info("Order with id [{}] has 0 items. Nothing to pay for.", orderId);
            return null;
        }
        AtomicLong sum = new AtomicLong(0L);
        itemsMap.forEach(item -> {
            if (item.getPrice() == null) {
                log.info("Price of item with id [{}] is null", item.getId());
                return;
            }
            if (item.getAmount() == null) {
                log.info("Amount of item with id [{}] is null", item.getId());
                return;
            }
            sum.addAndGet((long) item.getPrice() * item.getAmount());
//                counter.increment(item.getPrice() * item.getAmount());
        });

        log.info("Start request to external system for payment for order with id [{}]", orderId);
        TransactionResponse transactionResponse = externalSystemService.getPaymentTransactionResponse(sum.get());
        PaymentStatus status;
        UUID transactionId;
        Long timestamp = System.currentTimeMillis();

        if (transactionResponse == null) {
            log.info("External system returned NOT OK status for order with id [{}]", orderId);
            status = PaymentStatus.FAILED;
            transactionId = UUID.randomUUID();
        } else if (transactionResponse.getStatus() != TransactionStatus.SUCCESS) {
            status = PaymentStatus.FAILED;
            transactionId = transactionResponse.getId();
        } else {
            status = PaymentStatus.SUCCESS;
            transactionId = transactionResponse.getId();
            log.info("Setting status PAID for order with id [{}]", orderId);
            orderEntity.setStatus(OrderStatusEnum.PAID);

            userAccountFinancialLogRecordRepository.save(
                    UserAccountFinancialLogRecord.builder()
                            .paymentTransactionId(transactionId)
                            .amount((int) sum.get())
                            .type(FinancialOperationType.WITHDRAW)
                            .orderId(orderId)
                            .timestamp(timestamp)
                            .build()
            );
        }

        log.info("External system returned [{}] for order with id [{}]", status, orderId);


        List<PaymentLogRecordEntity> paymentHistory = orderEntity.getPaymentHistory();
        if (paymentHistory == null) {
            paymentHistory = new ArrayList<>();
        }

        PaymentLogRecordEntity paymentLogRecordEntity = new PaymentLogRecordEntity();
        paymentLogRecordEntity.setId(UUID.randomUUID());
        paymentLogRecordEntity.setAmount((int) sum.get());
        paymentLogRecordEntity.setTimestamp(timestamp);
        paymentLogRecordEntity.setStatus(status);
        paymentLogRecordEntity.setTransactionId(transactionId);
        paymentLogRecordEntity.setOrderEntity(orderEntity);

        paymentHistory.add(paymentLogRecordEntity);
        orderEntity.setPaymentHistory(paymentHistory);

        orderRepository.save(orderEntity);

        return new PaymentSubmissionDto(timestamp, transactionId);
    }
}
