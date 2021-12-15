package com.itmo.microservices.demo.payment.impl.service;


import com.itmo.microservices.demo.payment.PaymentServiceConstants;
import com.itmo.microservices.demo.payment.api.exception.PaymentServiceException;
import com.itmo.microservices.demo.payment.api.exception.TransactionException;
import com.itmo.microservices.demo.payment.api.model.FinancialOperationType;
import com.itmo.microservices.demo.payment.api.model.PaymentSubmissionDto;
import com.itmo.microservices.demo.payment.api.model.UserAccountFinancialLogRecordDto;
import com.itmo.microservices.demo.payment.api.service.PaymentService;
import com.itmo.microservices.demo.payment.externalPaymentService.service.ExternalService;
import com.itmo.microservices.demo.payment.externalPaymentService.service.exception.ExternalServiceException;
import com.itmo.microservices.demo.payment.impl.model.UserAccountFinancialLogRecord;
import com.itmo.microservices.demo.payment.impl.repository.UserAccountFinancialLogRecordRepository;
import com.itmo.microservices.demo.payment.utils.UserAccountFinancialLogRecordUtils;
import com.itmo.microservices.demo.users.api.exception.UserNotFoundException;
import com.itmo.microservices.demo.users.api.service.UserService;
import com.itmo.microservices.demo.users.impl.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final UserAccountFinancialLogRecordRepository userAccountFinancialLogRecordRepository;
    private final UserService userService;
    private final ExternalService externalService;

    @Value("${external-service.order-client-secret}")
    private String clientSecret;

    @Override
    public List<UserAccountFinancialLogRecordDto> getFinlog(String username, UUID orderId) throws UserNotFoundException {
        var user = getUserByUsername(username);

        var list = orderId != null ?
                userAccountFinancialLogRecordRepository.findAllByUserIdAndOrderId(user.getId(), orderId) :
                userAccountFinancialLogRecordRepository.findAllByUserId(user.getId()); //TODO:: criteria API? @Query?
        return list
                .stream()
                .map(UserAccountFinancialLogRecordUtils::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentSubmissionDto executeOrderPayment(String username, UUID orderId) throws UserNotFoundException, PaymentServiceException {
        var user = getUserByUsername(username);

        try {
            var externalServicePayment = externalService.executePayment(clientSecret).join();

            var finlog = UserAccountFinancialLogRecord.builder()
                    .userId(user.getId())
                    .orderId(orderId)
                    .paymentTransactionId(externalServicePayment.getId())
                    .type(FinancialOperationType.WITHDRAW)
                    .amount(1) // ???
                    .timestamp(externalServicePayment.getCompletedTime())
                    .build();

            userAccountFinancialLogRecordRepository.save(finlog);

            return PaymentSubmissionDto.builder()
                    .timestamp(finlog.getTimestamp())
                    .transactionID(finlog.getPaymentTransactionId())
                    .build();
        } catch (CompletionException e) {
            throw new TransactionException(e.getMessage());
        }
    }

    private AppUser getUserByUsername(String username) throws UserNotFoundException {
        var user = userService.getUser(username);

        if (user == null) {
            throw new UserNotFoundException(String.format("%s user with name: '%s' not found",
                    PaymentServiceConstants.PAYMENT_LOG_MARKER, username));
        }

        return user;
    }
}
