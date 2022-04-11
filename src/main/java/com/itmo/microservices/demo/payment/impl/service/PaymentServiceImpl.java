package com.itmo.microservices.demo.payment.impl.service;

import com.itmo.microservices.demo.lib.common.order.entity.OrderEntity;
import com.itmo.microservices.demo.lib.common.order.repository.OrderItemRepository;
import com.itmo.microservices.demo.lib.common.order.repository.OrderRepository;
import com.itmo.microservices.demo.order.api.service.OrderService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import com.itmo.microservices.demo.payment.PaymentServiceConstants;
import com.itmo.microservices.demo.payment.api.model.FinancialOperationType;
import com.itmo.microservices.demo.payment.api.model.PaymentSubmissionDto;
import com.itmo.microservices.demo.payment.api.model.UserAccountFinancialLogRecordDto;
import com.itmo.microservices.demo.payment.api.service.PaymentService;
import com.itmo.microservices.demo.payment.impl.model.UserAccountFinancialLogRecord;
import com.itmo.microservices.demo.payment.impl.repository.UserAccountFinancialLogRecordRepository;
import com.itmo.microservices.demo.payment.utils.UserAccountFinancialLogRecordUtils;
import com.itmo.microservices.demo.users.api.exception.UserNotFoundException;
import com.itmo.microservices.demo.users.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final UserAccountFinancialLogRecordRepository userAccountFinancialLogRecordRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final UserService userService;
    private final MeterRegistry meterRegistry;
    @Value("#{environment['service.name']}")
    private String serviceName = "";

    @Override
    public List<UserAccountFinancialLogRecordDto> getFinlog(String name, UUID orderId) throws UserNotFoundException {
        var user = userService.getUser(name);

        if (user == null) {
            throw new UserNotFoundException(String.format("%s user with name: '%s' not found",
                    PaymentServiceConstants.PAYMENT_LOG_MARKER, name));
        }

        userAccountFinancialLogRecordRepository.save(
                UserAccountFinancialLogRecord.builder()
                        .paymentTransactionId(UUID.randomUUID())
                        .amount(1)
                        .type(FinancialOperationType.REFUND)
                        .orderId(orderId != null ? orderId : UUID.randomUUID())
                        .timestamp(LocalDateTime.now())
                        .userId(user.getId())
                        .build()
        ); // temporary just to test

        var list = orderId != null ?
                userAccountFinancialLogRecordRepository.findAllByUserIdAndOrderId(user.getId(), orderId) :
                userAccountFinancialLogRecordRepository.findAllByUserId(user.getId()); //TODO:: criteria API? @Query?
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
//        Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(orderId);
//        if (optionalOrderEntity.isEmpty()) {
//            log.info("Order with id [{}] was not found", orderId);
//            return null;
//        }
//        var itemsMap = optionalOrderEntity.get().getItemsMap();
//        Counter counter = revenue.tags("serviceName", "p03").register(meterRegistry);
//        if (itemsMap == null) {
//            log.info("Order with id [{}] has 0 items. Nothing to pay for.", orderId);
//            return null;
//        }
//        if (itemsMap != null) {
//            itemsMap.forEach(item -> {
//                if (item.getPrice() == null) {
//                    log.info("Price of item with id [{}] is null", item.getId());
//                    return;
//                }
//                if (item.getAmount() == null) {
//                    log.info("Amount of item with id [{}] is null", item.getId());
//                    return;
//                }
//                counter.increment(item.getPrice() * item.getAmount());
//            });
//        }
        return PaymentSubmissionDto.builder()
                .timestamp(System.currentTimeMillis())
                .transactionID(UUID.randomUUID()) //TODO:: query to order repo by orderId
                .build();
    }
}
