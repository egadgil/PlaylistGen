package payment;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentInfoRepository paymentInfoRepository;

    public PaymentController(PaymentInfoRepository paymentInfoRepository) {
        this.paymentInfoRepository = paymentInfoRepository;
    }

    @PostMapping
    public String processPayment(@RequestBody PaymentInfo paymentInfo) {
        // Save payment information securely in the database
        paymentInfoRepository.save(paymentInfo);

        // Implement payment processing logic with a payment gateway here

        return "Payment successful";
    }
}
