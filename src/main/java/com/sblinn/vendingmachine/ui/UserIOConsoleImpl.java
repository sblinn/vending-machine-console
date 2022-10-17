
package com.sblinn.vendingmachine.ui;

import java.util.Scanner;


public class UserIOConsoleImpl implements UserIO {

    private final Scanner console = new Scanner(System.in);

    /**
     * Displays a String message on the console.
     *
     * @param msg - String of information to display to the user.
     */
    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    /**
     *
     * Displays a given String message on the console, then waits for response
     * from the user to return.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @return the answer to the message as string
     */
    @Override
    public String readString(String msgPrompt) {
        System.out.println(msgPrompt);
        return console.nextLine();
    }

    /**
     *
     * Takes in a message to display on the console, and continually re-prompts 
     * the user with that message until they enter an integer to be returned 
     * as the answer to that message.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @return the answer to the message as integer
     */
    @Override
    public int readInt(String msgPrompt) {
        boolean invalidInput = true;
        int num = 0;
        while (invalidInput) {
            try {
                // print the message msgPrompt 
                String stringValue = this.readString(msgPrompt);
                // Get the input line, and try and parse
                // if non-integer character is entered, exception will be caught
                num = Integer.parseInt(stringValue); 
                invalidInput = false; // or you can use 'break;'
            } catch (NumberFormatException e) {
                this.print("Invalid input. Please try again.");
            }
        }
        return num;
    }

    /**
     *
     * Takes in a message to display on the console, and continually re-prompts 
     * the user with that message until they enter an integer within the 
     * specified min/max range to be returned as the answer to that message.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @param min - minimum acceptable value for return
     * @param max - maximum acceptable value for return
     * @return an integer value as an answer to the message prompt within the min/max range
     */
    @Override
    public int readInt(String msgPrompt, int min, int max) {
//        int result;
//        do {
//            result = readInt(msgPrompt);
//        } while (result < min || result > max);
        
        int result = readInt(msgPrompt);
        while (result < min || result > max) {
            print("Invalid input. Please try again.");
            result = readInt(msgPrompt);
        }
        return result;
    }
    
}