package payment;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PaymentInfo {
    @Id
    private Long id;
    private String cardNumber;
    private String securityCode;
    private String expiryDate;
    private String fullName;


}
