package entities.bills.sys;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class BankAccount extends BillingDetail {
    private String name;
    private String swiftCode;

    public BankAccount() {
    }

    @Column (name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column (name = "swift_code")
    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
}
