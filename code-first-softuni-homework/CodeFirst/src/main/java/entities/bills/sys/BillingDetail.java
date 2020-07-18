package entities.bills.sys;

import entities.BaseEntity;

import javax.persistence.*;

@Entity
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "billing_details", schema = "bills_payment_system", catalog = "bills_payment_system")
public abstract class BillingDetail extends BaseEntity {
    private int number;
    private User user;

    //Uses DTYPE to differentiate between bank account and credit card.

    public BillingDetail() {
    }

    @Column (name = "number", nullable = false)
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
