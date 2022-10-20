package mzatka.bankappbackend.controllers;

import lombok.RequiredArgsConstructor;
import mzatka.bankappbackend.exceptions.*;
import mzatka.bankappbackend.models.dtos.Dto;
import mzatka.bankappbackend.models.dtos.MessageDto;
import mzatka.bankappbackend.models.dtos.TransferDto;
import mzatka.bankappbackend.models.entities.products.DebitCard;
import mzatka.bankappbackend.services.AccountService;
import mzatka.bankappbackend.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/atm")
@RequiredArgsConstructor
public class AtmController {

  private final ProductService productService;
  private final AccountService accountService;

  @PostMapping("/deposit")
  public ResponseEntity<Dto> depositCash(@RequestBody @Valid TransferDto transferDto) {
    if (productService.ibanNotExists(transferDto.getIban())) {
      throw new NoSuchProductWithIbanException("/deposit");
    }
    if (accountService.isAccountBlocked(
        productService.getCustomerByProduct(
            productService.getProductByIban(transferDto.getIban())))) {
      throw new AccountBlockedException("/deposit");
    }
    if (!productService.isProductDebitCard(transferDto.getIban())) {
      throw new ProductIsNotDebitOrCreditCardException("/withdraw");
    }
    DebitCard usedCard = (DebitCard) productService.getProductByIban(transferDto.getIban());
    if (!usedCard.getPinCode().equals(transferDto.getPinCode())) {
      throw new IncorrectPinCodeException("/deposit");
    }
    return ResponseEntity.ok(
        new MessageDto(
            String.format(
                "Cash deposited successfully. New balance is: %s %s",
                productService.depositCashAndReturnBalance(transferDto), usedCard.getCurrency())));
  }

  @PostMapping("/withdraw")
  public ResponseEntity<Dto> withdrawCash(@RequestBody @Valid TransferDto transferDto) {
    if (productService.ibanNotExists(transferDto.getIban())) {
      throw new NoSuchProductWithIbanException("/withdraw");
    }
    if (accountService.isAccountBlocked(
        productService.getCustomerByProduct(
            productService.getProductByIban(transferDto.getIban())))) {
      throw new AccountBlockedException("/withdraw");
    }
    if (!productService.isProductDebitCard(transferDto.getIban())) {
      throw new ProductIsNotDebitOrCreditCardException("/withdraw");
    }
    DebitCard usedCard = (DebitCard) productService.getProductByIban(transferDto.getIban());
    if (!usedCard.getPinCode().equals(transferDto.getPinCode())) {
      throw new IncorrectPinCodeException("/withdraw");
    }
    if (!productService.hasSufficientFunds(transferDto)) {
      throw new InsufficientFundsException("/withdraw");
    }
    return ResponseEntity.ok(
        new MessageDto(
            String.format(
                "Cash withdrawal successful. New balance is: %s %s",
                productService.withdrawCashAndReturnBalance(transferDto), usedCard.getCurrency())));
  }
}
