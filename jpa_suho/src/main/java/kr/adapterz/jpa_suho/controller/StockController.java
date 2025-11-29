package kr.adapterz.jpa_suho.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@RestController
@RequestMapping("/api/v1/stocks")
@CrossOrigin(origins = "http://127.0.0.1:5500") // 프론트엔드 origin 허용
public class StockController {

    @GetMapping("/{symbol}")
    public ResponseEntity<String> getStockPrice(@PathVariable String symbol) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol;

        try {
            // User-Agent 헤더 추가 (Yahoo Finance가 봇 차단할 수 있음)
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 에러 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to fetch stock data\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

