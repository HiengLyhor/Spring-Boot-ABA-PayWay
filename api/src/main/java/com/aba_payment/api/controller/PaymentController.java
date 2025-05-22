package com.aba_payment.api.controller;

import com.aba_payment.api.dto.aba.CallbackRequest;
import com.aba_payment.api.service.ABAPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/aba/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow all origins (replace "*" with frontend URL in production)
public class PaymentController {

    private final ABAPayService abaPayService;

    @GetMapping("generate-qr-image")
    ResponseEntity<byte[]> generateQrImage(@RequestParam double amount, @RequestParam String ccy) {
        return abaPayService.qrImage(amount, ccy);
    }

    @PostMapping("callback")
    void ExCallbackRequest(@RequestBody CallbackRequest request) {
        abaPayService.txnCallback(request);
    }

}
