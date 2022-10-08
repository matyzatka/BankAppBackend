package mzatka.bankappbackend.controllers;

import lombok.RequiredArgsConstructor;
import mzatka.bankappbackend.exceptions.InsufficientFundsException;
import mzatka.bankappbackend.exceptions.NoSuchProductWithIbanException;
import mzatka.bankappbackend.models.dtos.Dto;
import mzatka.bankappbackend.models.dtos.MessageDto;
import mzatka.bankappbackend.models.dtos.TransferDto;
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

  @PostMapping("/deposit")
  public ResponseEntity<Dto> depositCash(@RequestBody @Valid TransferDto transferDto) {
    if (productService.ibanNotExists(transferDto.getIban())) {
      throw new NoSuchProductWithIbanException("/deposit");
    }
    return ResponseEntity.ok(
        new MessageDto(
            String.format(
                "Cash deposited successfully. New balance is: %s",
                productService.depositCashAndReturnBalance(transferDto))));
  }

  @PostMapping("/withdraw")
  public ResponseEntity<Dto> withdrawCash(@RequestBody @Valid TransferDto transferDto) {
    if (productService.ibanNotExists(transferDto.getIban())) {
      throw new NoSuchProductWithIbanException("/deposit");
    }
    if (!productService.hasSufficientFunds(transferDto)) {
      throw new InsufficientFundsException("/withdraw");
    }
    return ResponseEntity.ok(
        new MessageDto(
            String.format(
                "Cash withdrawal successful. New balance is: %s",
                productService.withdrawCashAndReturnBalance(transferDto))));
  }
}
