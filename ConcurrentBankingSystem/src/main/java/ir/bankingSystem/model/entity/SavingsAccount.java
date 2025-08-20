package ir.bankingSystem.model.entity;

import jakarta.persistence.*;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "savings_account")
@Data
public class SavingsAccount extends BankAccount {

}