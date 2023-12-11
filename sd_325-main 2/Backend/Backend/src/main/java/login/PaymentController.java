package login;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@Api(tags = "Payment Controller", description = "Operations related to payment processing")
public class PaymentController {

    @Autowired
    private UserRepository userRepository;
    private final PaymentInfoRepository paymentInfoRepository;

    public PaymentController(PaymentInfoRepository paymentInfoRepository) {
        this.paymentInfoRepository = paymentInfoRepository;
    }

    @ApiOperation("Process payment-Esha Gadgil")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Payment successful"),
    })
    @PostMapping("/post_payment")
    public String processPayment(@RequestBody PaymentInfo paymentInfo, @RequestParam String username) {

        User existingUser = (User) userRepository.findByUsername(username);
        if (existingUser == null) {
            return "user not found";
        }

        existingUser.addPayment(paymentInfo);
        existingUser.setMembershipType("Paid Member");

        existingUser.setMembershipType("Paid Member");
        // Save payment information securely in the database
        paymentInfoRepository.save(paymentInfo);
        userRepository.save(existingUser);

        // Implement payment processing logic with a payment gateway here


        userRepository.save(existingUser);

        return "Payment successful";
    }
}
