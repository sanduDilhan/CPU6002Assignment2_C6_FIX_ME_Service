package com.sentura.bLow.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

    public String generateRandomString(int length) {
        int leftLimit = 97;
        int rightLimit = 122;
        Random random = new Random();
        return random.ints(leftLimit,rightLimit+1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();
    }

    public String generateRandomNumber() {
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        return n+"";
    }

    public String getDateTimeString(Date date) {
        return dateTimeFormat.format(date);
    }

    public String getDateString(Date date) {
        return dateFormat.format(date);
    }

    public String getDecimalString(double value) {
        return decimalFormat.format(value);
    }

    public Date getDateObject(String date) throws Exception{
        return dateFormat.parse(date);
    }

    public Date getDateTimeObject(String dateTimeString) throws ParseException {
        return dateTimeFormat.parse(dateTimeString);
    }
    public String getDateTimeStringAmPm(Date date) {

        return dateTimeFormat.format(date);
    }

    public Double getNumericValue(String input){
        // Regular expression to match numeric and non-numeric parts
        String regex = "(\\d+)(\\D+)";

        // Compile the regular expression
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);

        // Create a matcher for the input string
        java.util.regex.Matcher matcher = pattern.matcher(input);

        // Check if the pattern matches
        if (matcher.matches()) {
            // Extract and print the numeric and non-numeric parts
            Double numericPart = Double.valueOf(matcher.group(1)); // First group is numeric
//            String nonNumericPart = matcher.group(2); // Second group is non-numeric
//            System.out.println("Numeric Part: " + numericPart);
//            System.out.println("Non-Numeric Part: " + nonNumericPart);
            return numericPart;
        } else {
            System.out.println("The input does not match the expected pattern.");
            return 0.0;
        }
    }

    public String getNonNumericValue(String input){
        // Pattern to match non-numeric characters including spaces
        String regex = "[^\\d]+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            sb.append(matcher.group()).append(" ");
        }

        String result = sb.toString().trim();

        return result;
    }
}