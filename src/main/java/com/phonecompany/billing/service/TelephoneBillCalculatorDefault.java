package com.phonecompany.billing.service;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class TelephoneBillCalculatorDefault implements TelephoneBillCalculator {


    public void processCSVFile() {
        try {
            CSVReader reader = new CSVReader(new FileReader("call-list.csv"));
            List<String[]> csvBody = reader.readAll();

            for (String[] line : csvBody) {
                String lineString = Arrays.toString(line)
                        .replace("[", "")
                        .replace("]", "");
                System.out.println(">>>>>>");
                System.out.println("phone log: " + lineString);
                calculate(lineString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BigDecimal calculate(String phoneLog) {

        String[] phoneLogParts = phoneLog.split(",");

        String callNumber = phoneLogParts[0].trim();
        String callBegin = phoneLogParts[1].trim();
        String callEnd = phoneLogParts[2].trim();

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime dateTimeBegin = LocalDateTime.parse(callBegin, formatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(callEnd, formatter);

        System.out.println("volane cislo " + callNumber);
        System.out.println("zahajeni hovoru " + dateTimeBegin);
        System.out.println("konec hovoru " + dateTimeEnd);

        Duration duration = Duration.between(dateTimeBegin, dateTimeEnd);
        System.out.println("vteriny " + duration.getSeconds());

        LocalTime intervalStart = LocalTime.of(8, 0);
        LocalTime intervalEnd = LocalTime.of(16, 0);

        int minutes = (int) (duration.getSeconds() / 60);
        System.out.println("minuty " + minutes);


        double price = 0D;

        for (int i = 0; i <= minutes && i < 4; i++) {
            if (dateTimeBegin.plusMinutes(i).toLocalTime().isAfter(intervalStart) &&
                    dateTimeBegin.plusMinutes(i).toLocalTime().isBefore(intervalEnd)) {
                price = price + 1;
                System.out.println("+ 1 - " + price);

            } else {
                price = price + 0.5;
                System.out.println("+ 0.5 - " + price);
            }
        }
        for (int i = 4; i <= minutes; i++) {
            price = price + 0.2;
            System.out.println("+ 0.2 - " + price);
        }
        System.out.println("celkem " + price);
        System.out.println("");

        return new BigDecimal(price);
    }
}
