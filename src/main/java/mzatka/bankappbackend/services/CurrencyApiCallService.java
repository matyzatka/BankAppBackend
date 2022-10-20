package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.dtos.ApiResponseDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.time.LocalDate;

public interface CurrencyApiCallService {

  @GET("{date}/currencies/eur.json")
  Call<ApiResponseDto> getCurrencies(@Path("date") LocalDate date);
}
