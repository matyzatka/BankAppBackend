package mzatka.bankappbackend.utilities;

import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PinUtilities {

  public static String generatePin() {
    return IntStream.range(0, 4)
        .mapToObj(i -> String.valueOf((int) (Math.random() * 9)))
        .collect(Collectors.joining());
  }
}
