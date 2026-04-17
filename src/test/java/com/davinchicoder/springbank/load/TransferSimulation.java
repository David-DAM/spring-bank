package com.davinchicoder.springbank.load;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.UUID;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class TransferSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .contentTypeHeader("application/xml");

    ScenarioBuilder scenario = scenario("Bank Transfers Load Test")
            .exec(
                    http("Create Transaction")
                            .post("/api/v1/transaction")
                            .body(StringBody(session -> buildXml()))
                            .check(
                                    status().is(200),
                                    responseTimeInMillis().lt(500)
                            )
            )
            .pause(1, 3);

    {
        setUp(
                scenario.injectOpen(
                        nothingFor(5),
                        rampUsers(200).during(10),
                        constantUsersPerSec(100).during(30)
                )
        ).protocols(httpProtocol);
    }

    private String buildXml() {
        return """
                    <Transaction xmlns="http://bank.com/transaction">
                        <id>%s</id>
                        <fromAccount>ES%s</fromAccount>
                        <toAccount>ES%s</toAccount>
                        <amount>%d</amount>
                        <type>DEBIT</type>
                        <createdAt>2026-03-18T10:45:30</createdAt>
                    </Transaction>
                """.formatted(
                UUID.randomUUID(),
                randomAccount(),
                randomAccount(),
                randomAmount()
        );
    }

    private String randomAccount() {
        return String.valueOf(100000000 + (int) (Math.random() * 900000000));
    }

    private int randomAmount() {
        return 10 + (int) (Math.random() * 1000);
    }
}