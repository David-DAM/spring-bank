package com.davinchicoder.springbank.load;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Session;
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

    FeederBuilder<String> feeder = csv("load/accounts.csv").circular();

    ScenarioBuilder bankTransferScenario = scenario("Bank Transfers Load Test")
            .feed(feeder)
            .exec(
                    http("Create Transaction")
                            .post("/api/v1/transaction")
                            .body(StringBody(this::buildXml))
                            .header("Idempotency-Key", _ -> UUID.randomUUID().toString())
                            .check(
                                    status().is(200),
                                    responseTimeInMillis().lt(500),
                                    bodyString().notNull()
                            )
            )
            .pause(1, 3);

    {
        setUp(
                bankTransferScenario.injectOpen(
                        nothingFor(5),
                        rampUsers(200).during(10),
                        constantUsersPerSec(100).during(30)
                )
        ).protocols(httpProtocol);
    }

    private String buildXml(Session session) {
        return """
                    <Transaction xmlns="http://bank.com/transaction">
                        <id>%s</id>
                        <fromAccount>%s</fromAccount>
                        <toAccount>%s</toAccount>
                        <amount>%d</amount>
                        <type>DEBIT</type>
                        <createdAt>2026-03-18T10:45:30</createdAt>
                    </Transaction>
                """.formatted(
                UUID.randomUUID(),
                session.getString("fromAccount"),
                session.getString("toAccount"),
                session.getInt("amount")
        );
    }
}