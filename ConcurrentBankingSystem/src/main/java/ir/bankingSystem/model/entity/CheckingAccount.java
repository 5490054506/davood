package ir.bankingSystem.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "checking_account")
@Data
public class CheckingAccount extends BankAccount {


}