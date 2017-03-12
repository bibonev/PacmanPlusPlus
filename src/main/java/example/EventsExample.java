package main.java.example;

import java.util.Scanner;

import main.java.event.Event;

public class EventsExample {
	public static void main(final String[] args) {
		final UserInputListener listener1 = new LengthListener();
		final UserInputListener listener2 = new PrinterListener();

		final Event<UserInputListener, String> userInputEvent = new Event<>((listener, s) -> listener.onUserInput(s));

		userInputEvent.addListener(listener1);
		userInputEvent.addListener(listener2);

		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				final String line = scanner.nextLine();
				if (line.length() == 0) {
					break;
				}

				userInputEvent.fire(line);
			}
		}
	}

	/**
	 * An event listener which prints the length of input entered by ythe user
	 */
	public static class LengthListener implements UserInputListener {
		@Override
		public void onUserInput(final String input) {
			System.out.println("The length of the input was " + input.length() + " characters");
		}
	}

	/**
	 * An event listener which prints any input entered by the user
	 */
	public static class PrinterListener implements UserInputListener {
		@Override
		public void onUserInput(final String input) {
			System.out.println("Input detected: " + input);
		}
	}
}
