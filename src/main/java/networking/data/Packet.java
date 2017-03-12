package main.java.networking.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A simple class that allows a packet (with a name, as a string, along with a
 * series of parameters as an associative array) to be parsed from, and written
 * to, a {@link String} for sending/receiving over a network.
 *
 * @author Tom Galvin
 */
public class Packet {
	private final String commandName;
	private final Map<String, CommandParameter> params;

	/**
	 * Initialize a new Packet object with the given packet name.
	 *
	 * @param commandName
	 *            The name of the packet.
	 */
	public Packet(final String commandName) {
		this.commandName = commandName;
		params = new HashMap<String, CommandParameter>();
	}

	/**
	 * Get the name of the packet.
	 *
	 * @return The name of the packet.
	 */
	public String getPacketName() {
		return commandName;
	}

	/**
	 * Gets a set of all the parameters contained within this packet.
	 *
	 * @return The name of each parameter sent with this packet.
	 */
	public Set<String> getParameterNames() {
		return params.keySet();
	}

	/**
	 * Determines if the packet contains a parameter with the given name.
	 *
	 * @param parameterName
	 *            The name of the parameter for whom to check the existence.
	 * @return {@code true} if the given parameter exists in this packet.
	 */
	public boolean hasParameter(final String parameterName) {
		return params.containsKey(parameterName);
	}

	/**
	 * Gets the type of the parameter with the given name.
	 *
	 * @param parameterName
	 *            The name of the parameter for whom to check the type of.
	 * @return The type of the parameter.
	 * @throws IllegalArgumentException
	 *             When the parameter with the given name doesn't exist within
	 *             this packet.
	 */
	public CommandParameterType getParameterType(final String parameterName) {
		if (!hasParameter(parameterName)) {
			return params.get(parameterName).getType();
		} else {
			throw new IllegalArgumentException(
					"Parameter named " + parameterName + " does not " + "exist in this packet.");
		}
	}

	/**
	 * Get the value of {@code paramName}, formatted as a string. This method
	 * will also ensure the parameter is of the correct type.
	 *
	 * @param type
	 *            The expected type of the parameter.
	 * @param paramName
	 *            The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException
	 *             When the parameter does not exist, or if it is of the wrong
	 *             type.
	 */
	private String getParameterValue(final CommandParameterType type, final String paramName) {
		if (params.containsKey(paramName)) {
			final CommandParameter param = params.get(paramName);
			if (param.getType() == type) {
				return param.getValue();
			} else {
				throw new IllegalArgumentException("Attempted to read parameter \"" + paramName + "\" of packet \""
						+ getPacketName() + "\" as " + "type " + type.name() + ", but it's actually of type "
						+ param.getType().name() + ".");
			}
		} else {
			throw new IllegalArgumentException("Attempted to read parameter \"" + paramName + "\" of packet \""
					+ getPacketName() + "\", " + "but it does not exist.");
		}
	}

	/**
	 * Gets the String parameter of the given name,
	 *
	 * @param paramName
	 *            The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException
	 *             When {@code paramName} is not a String or doesnt exist.
	 */
	public String getString(final String paramName) {
		return getParameterValue(CommandParameterType.STRING, paramName);
	}

	/**
	 * Gets the integer parameter of the given name,
	 *
	 * @param paramName
	 *            The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException
	 *             When {@code paramName} is not an integer or doesnt exist.
	 */
	public int getInteger(final String paramName) {
		return Integer.valueOf(getParameterValue(CommandParameterType.INTEGER, paramName));
	}

	/**
	 * Gets the long parameter of the given name,
	 *
	 * @param paramName
	 *            The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException
	 *             When {@code paramName} is not a long or doesnt exist.
	 */
	public long getLong(final String paramName) {
		return Long.valueOf(getParameterValue(CommandParameterType.LONG, paramName));
	}

	/**
	 * Gets the float parameter of the given name,
	 *
	 * @param paramName
	 *            The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException
	 *             When {@code paramName} is not a float or doesnt exist.
	 */
	public float getFloat(final String paramName) {
		return Float.valueOf(getParameterValue(CommandParameterType.FLOAT, paramName));
	}

	/**
	 * Gets the double parameter of the given name,
	 *
	 * @param paramName
	 *            The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException
	 *             When {@code paramName} is not a double or doesnt exist.
	 */
	public double getDouble(final String paramName) {
		return Double.valueOf(getParameterValue(CommandParameterType.DOUBLE, paramName));
	}

	/**
	 * Gets the boolean parameter of the given name,
	 *
	 * @param paramName
	 *            The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException
	 *             When {@code paramName} is not a boolean or doesnt exist.
	 */
	public boolean getBoolean(final String paramName) {
		return Boolean.valueOf(getParameterValue(CommandParameterType.BOOLEAN, paramName));
	}

	/**
	 * Sets the parameter {@code paramName} to {@code value}, as well as noting
	 * its data type as {@code type}.
	 *
	 * @param type
	 *            The type of data that {@code value} represents.
	 * @param paramName
	 *            The name of the parameter.
	 * @param value
	 *            The value of the parameter.
	 */
	private void setParameterValue(final CommandParameterType type, final String paramName, final String value) {
		if (!params.containsKey(paramName)) {
			params.put(paramName, new CommandParameter(type, value));
		} else {
			throw new IllegalArgumentException("This packet already has a parameter named " + paramName + ".");
		}
	}

	/**
	 * Sets a parameter as a string.
	 *
	 * @param paramName
	 *            The name of the parameter.
	 * @param value
	 *            The value of the parameter.
	 * @return This packet, for chaining {@code set*} commands.
	 * @throws IllegalArgumentException
	 *             When the parameter already exists.
	 */
	public Packet setString(final String paramName, final String value) {
		setParameterValue(CommandParameterType.STRING, paramName, value);
		return this;
	}

	/**
	 * Sets a parameter as an integer..
	 *
	 * @param paramName
	 *            The name of the parameter.
	 * @param value
	 *            The value of the parameter.
	 * @return This packet, for chaining {@code set*} commands.
	 * @throws IllegalArgumentException
	 *             When the parameter already exists.
	 */
	public Packet setInteger(final String paramName, final int value) {
		setParameterValue(CommandParameterType.INTEGER, paramName, String.valueOf(value));
		return this;
	}

	/**
	 * Sets a parameter as a long.
	 *
	 * @param paramName
	 *            The name of the parameter.
	 * @param value
	 *            The value of the parameter.
	 * @return This packet, for chaining {@code set*} commands.
	 * @throws IllegalArgumentException
	 *             When the parameter already exists.
	 */
	public Packet setLong(final String paramName, final long value) {
		setParameterValue(CommandParameterType.LONG, paramName, String.valueOf(value));
		return this;
	}

	/**
	 * Sets a parameter as a float.
	 *
	 * @param paramName
	 *            The name of the parameter.
	 * @param value
	 *            The value of the parameter.
	 * @return This packet, for chaining {@code set*} commands.
	 * @throws IllegalArgumentException
	 *             When the parameter already exists.
	 */
	public Packet setFloat(final String paramName, final float value) {
		setParameterValue(CommandParameterType.FLOAT, paramName, String.valueOf(value));
		return this;
	}

	/**
	 * Sets a parameter as a double.
	 *
	 * @param paramName
	 *            The name of the parameter.
	 * @param value
	 *            The value of the parameter.
	 * @return This packet, for chaining {@code set*} commands.
	 * @throws IllegalArgumentException
	 *             When the parameter already exists.
	 */
	public Packet setDouble(final String paramName, final double value) {
		setParameterValue(CommandParameterType.DOUBLE, paramName, String.valueOf(value));
		return this;
	}

	/**
	 * Sets a parameter as a boolean.
	 *
	 * @param paramName
	 *            The name of the parameter.
	 * @param value
	 *            The value of the parameter.
	 * @return This packet, for chaining {@code set*} commands.
	 * @throws IllegalArgumentException
	 *             When the parameter already exists.
	 */
	public Packet setBoolean(final String paramName, final boolean value) {
		setParameterValue(CommandParameterType.BOOLEAN, paramName, String.valueOf(value));
		return this;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getPacketName());
		builder.append("`|");

		boolean firstParam = true;
		for (final String key : params.keySet()) {
			if (firstParam) {
				firstParam = false;
			} else {
				builder.append("`,");
			}
			builder.append(key);
			builder.append("`=");
			builder.append(params.get(key).toString());
		}
		return builder.toString();
	}

	/**
	 * Parses a packet from a String. {@code string} can be a value returned by
	 * {@link Packet#toString()}.
	 *
	 * @param string
	 *            The string representing the packet.
	 * @return The packet that is represented by {@code string}.
	 */
	public static Packet fromString(final String string) {
		final int paramsBeginIndex = string.indexOf("`|");
		final String commandName = string.substring(0, paramsBeginIndex);
		final Packet packet = new Packet(commandName);
		final String commandParamsString = string.substring(paramsBeginIndex + 2);

		final String[] commandParams = StringUtil.split(commandParamsString, "`,", true);
		/*
		 * If there are no parameters, then the array will be empty, as the
		 * third parameter to {@link StringUtil.split} specifies whether to
		 * remove empty substrings or not.
		 */
		for (final String commandParam : commandParams) {
			final int paramBeginIndex = commandParam.indexOf("`=");

			final String paramName = commandParam.substring(0, paramBeginIndex);
			final String paramData = commandParam.substring(paramBeginIndex + 2);
			final CommandParameter parameter = CommandParameter.fromString(paramData);
			packet.params.put(paramName, parameter);
		}

		return packet;
	}

	/**
	 * Represents the parameter of a packet.
	 *
	 * @author Tom Galvin
	 */
	private static class CommandParameter {
		private final CommandParameterType type;
		private final String value;

		/**
		 * Create a new packet parameter.
		 *
		 * @param type
		 *            The type of the packet.
		 * @param value
		 *            The value of the packet.
		 */
		public CommandParameter(final CommandParameterType type, final String value) {
			if (value == null && !type.isNullable()) {
				throw new IllegalArgumentException("Parameters of type " + type.toString() + " may not be null.");
			} else {
				this.type = type;
				this.value = value;
			}
		}

		/**
		 * Get the value of the packet.
		 *
		 * @return The value of the packet.
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Gets the type of the packet, represented using the
		 * {@link CommandParameterType} enumeration which represents all of the
		 * allowable types in a {@link Packet} object.
		 *
		 * @return The type of the packet.
		 */
		public CommandParameterType getType() {
			return type;
		}

		@Override
		public String toString() {
			if (getValue() == null) {
				return getType().toCommandString() + "`!";
			} else {
				return getType().toCommandString() + "`:" + getValue().replace("`", "{`}");
			}
		}

		/**
		 * Creates a new {@link CommandParameter} from a string representing the
		 * data within the parameter. {@code string} can be a value returned by
		 * {@link CommandParameter#toString()}.
		 *
		 * @param string
		 *            The string representing the data of this packet parameter.
		 * @return The packet parameter represented by {@code string}.
		 */
		public static CommandParameter fromString(final String string) {
			if (string.endsWith("`!")) {
				final String typeString = string.substring(0, string.length() - 2);
				final CommandParameterType type = CommandParameterType.fromString(typeString);
				return new CommandParameter(type, null);
			} else {
				final int separatorIndex = string.indexOf("`:");
				final String typeString = string.substring(0, separatorIndex);
				final CommandParameterType type = CommandParameterType.fromString(typeString);
				final String value = string.substring(separatorIndex + 2).replace("{`}", "`");
				return new CommandParameter(type, value);
			}
		}
	}

	/**
	 * Represents the allowable types of packet parameters.
	 *
	 * @author Tom Galvin
	 */
	public static enum CommandParameterType {
		/**
		 * Represents parameters of type String. String-valued parameters may be
		 * null.
		 */
		STRING("s", true),
		/**
		 * Represents parameters of type integer.
		 */
		INTEGER("i", false),
		/**
		 * Represents parameters of type long.
		 */
		LONG("l", false),
		/**
		 * Represents parameters of type float.
		 */
		FLOAT("f", false),
		/**
		 * Represents parameters of type double.
		 */
		DOUBLE("d", false),
		/**
		 * Represents parameters of type boolean.
		 */
		BOOLEAN("b", false);

		private boolean nullable;
		private String stringRepresentation;

		/**
		 * Create a new variant of the {@link Packet} enum.
		 *
		 * @param stringRepresentation
		 *            The representation of this type, as a string.
		 * @param nullable
		 *            Whether this specific type is allowed to be {@code null}
		 *            or not.
		 */
		private CommandParameterType(final String stringRepresentation, final boolean nullable) {
			this.stringRepresentation = stringRepresentation;
			this.nullable = nullable;
		}

		/**
		 * Gets whether this type may be null-valued.
		 *
		 * @return {@code true} if this type is nullable.
		 */
		public boolean isNullable() {
			return nullable;
		}

		/**
		 * Converts this type to a name suitable for representation in the
		 * serialized form of a {@link Packet}.
		 *
		 * @return A short String form of this type.
		 */
		public String toCommandString() {
			return stringRepresentation;
		}

		/**
		 * Creates an instance of {@link CommandParameterType} from the String
		 * representing it.
		 *
		 * @param string
		 *            The string representing the type of the argument.
		 * @return The type of the argument.
		 */
		public static CommandParameterType fromString(final String string) {
			/*
			 * For network and processing brevity, the type names have been
			 * condensed down to single letters. We can therefore just switch on
			 * the first character in the string.
			 */

			final char letter = string.charAt(0);
			switch (letter) {
			case 's':
				return STRING;
			case 'i':
				return INTEGER;
			case 'l':
				return LONG;
			case 'f':
				return FLOAT;
			case 'd':
				return DOUBLE;
			case 'b':
				return BOOLEAN;
			default:
				throw new IllegalArgumentException("Invalid type name: " + string);
			}
		}
	}
}
