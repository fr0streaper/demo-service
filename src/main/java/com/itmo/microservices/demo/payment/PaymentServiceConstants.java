package com.itmo.microservices.demo.payment;

import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true)
public class PaymentServiceConstants {

    // logging
    public static String PAYMENT_LOG_MARKER = "[PAYMENT]";
    public static String EXTERNAL_SERVICE_MARKER = "[EXTERNAL_SERVICE]";

    // rest template
    public static String EXTERNAL_SERVICE_TRANSACTIONS_URL = "/transactions";
}
