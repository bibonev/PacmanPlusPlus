package teamproject.networking.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A simple class that allows a packet (with a name, as a string, along with
 * a series of parameters as an associative array) to be parsed from, and
 * written to, a {@link String} for sending/receiving over a network.
 * 
 * @author Tom Galvin
 */
public class Packet {
	private final String commandName;
	private final Map<String, CommandParameter> params;
	
	/**
	 * Initialize a new Packet object with the given command name.
	 * 
	 * @param commandName The name of the command.
	 */
	public Packet(String commandName) {
		this.commandName = commandName;
		this.params = new HashMap<String, CommandParameter>();
	}
	
	/**
	 * Get the name of the command.
	 * 
	 * @return The name of the command.
	 */
	public String getCommandName() {
		return commandName;
	}
	
	/**
	 * Gets a set of all the parameters contained within this command.
	 * 
	 * @return The name of each parameter sent with this command.
	 */
	public Set<String> getParameterNames() {
		return params.keySet();
	}
	
	/**
	 * Determines if the command contains a parameter with the given name.
	 * 
	 * @param parameterName The name of the parameter for whom to check the
	 * existence.
	 * @return {@code true} if the given parameter exists in this command.
	 */
	public boolean hasParameter(String parameterName) {
		return params.containsKey(parameterName);
	}
	
	/**
	 * Gets the type of the parameter with the given name.
	 * 
	 * @param parameterName The name of the parameter for whom to check the
	 * type of.
	 * @return The type of the parameter.
	 * @throws IllegalArgumentException When the parameter with the given name
	 * doesn't exist within this command.
	 */
	public CommandParameterType getParameterType(String parameterName) {
		if(!hasParameter(parameterName)) {
			return params.get(parameterName).getType();
		} else {
			throw new IllegalArgumentException(
					"Parameter named " + parameterName + " does not " +
					"exist in this command.");
		}
	}
	
	/**
	 * Get the value of {@code paramName}, formatted as a string. This method
	 * will also ensure the parameter is of the correct type.
	 * 
	 * @param type The expected type of the parameter.
	 * @param paramName The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException When the parameter does not exist, or
	 * if it is of the wrong type.
	 */
	private String getParameterValue(CommandParameterType type, String paramName) {
		if(params.containsKey(paramName)) {
			CommandParameter param = params.get(paramName);
			if(param.getType() == type) {
				return param.getValue();
			} else {
				throw new IllegalArgumentException(
						"Attempted to read parameter \"" + paramName + "\" of command \"" +
						this.getCommandName() + "\" as " +
						"type " + type.name() + ", but it's actually of type " + param.getType().name() + ".");
			}
		} else {
			throw new IllegalArgumentException(
					"Attempted to read parameter \"" + paramName + "\" of command \"" + this.getCommandName() + "\", " +
					"but it does not exist.");
		}
	}
	
	/**
	 * Gets the String parameter of the given name,
	 * 
	 * @param paramName The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException When {@code paramName} is not a
	 * String or doesnt exist.
	 */
	public String getString(String paramName) {
		return getParameterValue(CommandParameterType.STRING, paramName);
	}

	/**
	 * Gets the integer parameter of the given name,
	 * 
	 * @param paramName The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException When {@code paramName} is not an
	 * integer or doesnt exist.
	 */
	public int getInteger(String paramName) {
		return Integer.valueOf(
				getParameterValue(CommandParameterType.INTEGER, paramName));
	}

	/**
	 * Gets the long parameter of the given name,
	 * 
	 * @param paramName The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException When {@code paramName} is not a
	 * long or doesnt exist.
	 */
	public long getLong(String paramName) {
		return Long.valueOf(
				getParameterValue(CommandParameterType.LONG, paramName));
	}

	/**
	 * Gets the float parameter of the given name,
	 * 
	 * @param paramName The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException When {@code paramName} is not a
	 * float or doesnt exist.
	 */
	public float getFloat(String paramName) {
		return Float.valueOf(
				getParameterValue(CommandParameterType.FLOAT, paramName));
	}

	/**
	 * Gets the double parameter of the given name,
	 * 
	 * @param paramName The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException When {@code paramName} is not a
	 * double or doesnt exist.
	 */
	public double getDouble(String paramName) {
		return Double.valueOf(
				getParameterValue(CommandParameterType.DOUBLE, paramName));
	}

	/**
	 * Gets the boolean parameter of the given name,
	 * 
	 * @param paramName The name of the parameter.
	 * @return The value of the parameter.
	 * @throws IllegalArgumentException When {@code paramName} is not a
	 * boolean or doesnt exist.
	 */
	public boolean getBoolean(String paramName) {
		return Boolean.valueOf(
				getParameterValue(CommandParameterType.BOOLEAN, paramName));
	}
	
	/**
	 * Sets the parameter {@code paramName} to {@code value}, as well as noting
	 * its data type as {@code type}.
	 * 
	 * @param type The type of data that {@code value} represents.
	 * @param paramName The name of the parameter.
	 * @param value The value of the parameter.
	 */
	private void setParameterValue(CommandParameterType type, String paramName, String value) {
		if(!params.containsKey(paramName)) {
			params.put(paramName, new CommandParameter(type, value));
		} else {
			throw new IllegalArgumentException(
					"This command already has a parameter named " +
					paramName + ".");
		}
	}
	
	/**
	 * Sets a parameter as a string.
	 * 
	 * @param paramName The name of the parameter.
	 * @param value The value of the parameter.
	 * @return This command, for chaining {@code set*} commands.
	 * @throws IllegalArgumentException When the parameter already exists.
	 */
	public Packet setString(String paramName, String value) {
		setParameterValue(CommandParameterType.STRING, paramName, value);
		return this;
	}

	/**
	 * Sets a parameter as an integer..
	 * 
	 * @param paramName The name of the parameter.
	 * @param value The value of the parameter.
	 * @return This command, for chaining {@code set*} commands.
	 * @throws IllegalArgumentException When the parameter already exists.
	 */
	public Packet setInteger(String paramName, int value) {
		setParameterValue(CommandParameterType.INTEGER, paramName, String.valueOf(value));
		return this;
	}
	
	/**
	 * Sets a parameter as a long.
	 * 
	 * @param paramName The name of the parameter.
	 * @param value The value of the parameter.
	 * @return This command, for chaining {@code set*} commands.
	 * @throws IllegalArgumentException When the parameter already exists.
	 */
	public Packet setLong(String paramName, long value) {
		setParameterValue(CommandParameterType.LONG, paramName, String.valueOf(value));
		return this;
	}

	/**
	 * Sets a parameter as a float.
	 * 
	 * @param paramName The name of the parameter.
	 * @param value The value of the parameter.
	 * @return This command, for chaining {@code set*} commands.
	 * @throws IllegalArgumentException When the parameter already exists.
	 */
	public Packet setFloat(String paramName, float value) {
		setParameterValue(CommandParameterType.FLOAT, paramName, String.valueOf(value));
		return this;
	}

	/**
	 * Sets a parameter as a double.
	 * 
	 * @param paramName The name of the parameter.
	 * @param value The value of the parameter.
	 * @return This command, for chaining {@code set*} commands.
	 * @throws IllegalArgumentException When the parameter already exists.
	 */
	public Packet setDouble(String paramName, double value) {
		setParameterValue(CommandParameterType.DOUBLE, paramName, String.valueOf(value));
		return this;
	}

	/**
	 * Sets a parameter as a boolean.
	 * 
	 * @param paramName The name of the parameter.
	 * @param value The value of the parameter.
	 * @return This command, for chaining {@code set*} commands.
	 * @throws IllegalArgumentException When the parameter already exists.
	 */
	public Packet setBoolean(String paramName, boolean value) {
		setParameterValue(CommandParameterType.BOOLEAN, paramName, String.valueOf(value));
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getCommandName());
		builder.append("`|");
		
		boolean firstParam = true;
		for(String key : params.keySet()) {
			if(firstParam) {
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
	 * Parses a command from a String. {@code string} can be a value returned
	 * by {@link Packet#toString()}.
	 * 
	 * @param string The string representing the command.
	 * @return The command that is represented by {@code string}.
	 */
	public static Packet fromString(String string) {
		int paramsBeginIndex = string.indexOf("`|");
		String commandName = string.substring(0, paramsBeginIndex);
		Packet command = new Packet(commandName);
		String commandParamsString = string.substring(paramsBeginIndex + 2);
		
		String[] commandParams = StringUtil.split(commandParamsString, "`,", true);
		/*
		 * If there are no parameters, then the array will be empty, as the
		 * third parameter to {@link StringUtil.split} specifies whether to
		 * remove empty substrings or not.
		 */
		for(int i = 0; i < commandParams.length; i++) {
			String commandParam = commandParams[i];
			int paramBeginIndex = commandParam.indexOf("`=");
			
			String paramName = commandParam.substring(0, paramBeginIndex);
			String paramData = commandParam.substring(paramBeginIndex + 2);
			CommandParameter parameter = CommandParameter.fromString(paramData);
			command.params.put(paramName, parameter);
		}
		
		return command;
	}
	
	/**
	 * Represents the parameter of a command.
	 * 
	 * @author Tom Galvin
	 */
	private static class CommandParameter {
		private final CommandParameterType type;
		private final String value;
		
		/**
		 * Create a new command parameter.
		 * 
		 * @param type The type of the command.
		 * @param value The value of the command.
		 */
		public CommandParameter(CommandParameterType type, String value) {
			if(value == null && !type.isNullable()) {
				throw new IllegalArgumentException("Parameters of type " +
						type.toString() + " may not be null.");
			} else {
				this.type = type;
				this.value = value;
			}
		}
		
		/**
		 * Get the value of the command.
		 * 
		 * @return The value of the command.
		 */
		public String getValue() {
			return this.value;
		}
		
		/**
		 * Gets the type of the command, represented using the {@link
		 * CommandParameterType} enumeration which represents all of the
		 * allowable types in a {@link Packet} object.
		 * 
		 * @return The type of the command.
		 */
		public CommandParameterType getType() {
			return this.type;
		}
		
		public String toString() {
			if(getValue() == null) {
				return getType().toCommandString() + "`!";
			} else {
				return getType().toCommandString() + "`:" + getValue().replace("`", "{`}");
			}
		}
		
		/**
		 * Creates a new {@link CommandParameter} from a string representing
		 * the data within the parameter. {@code string} can be a value
		 * returned by {@link CommandParameter#toString()}.
		 * 
		 * @param string The string representing the data of this command
		 * parameter.
		 * @return The command parameter represented by {@code string}.
		 */
		public static CommandParameter fromString(String string) {
			if(string.endsWith("`!")) {
				String typeString = string.substring(0, string.length() - 2);
				CommandParameterType type = CommandParameterType.fromString(typeString);
				return new CommandParameter(
						type,
						null);
			} else {
				int separatorIndex = string.indexOf("`:");
				String typeString = string.substring(0, separatorIndex);
				CommandParameterType type = CommandParameterType.fromString(typeString);
				String value = string.substring(separatorIndex + 2).replace("{`}", "`");
				return new CommandParameter(type, value);
			}
		}
	}
	
	/**
	 * Represents the allowable types of command parameters.
	 * 
	 * @author Tom Galvin
	 */
	public static enum CommandParameterType {
		/**
		 * Represents parameters of type String.
		 * String-valued parameters may be null.
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
		 * Represents parameters of type  boolean.
		 */
		BOOLEAN("b", false);
		
		private boolean nullable;
		private String stringRepresentation;
		
		/**
		 * Create a new variant of the {@link Packet} enum.
		 * 
		 * @param stringRepresentation The representation of this type, as
		 * a string.
		 * @param nullable Whether this specific type is allowed to be
		 * {@code null} or not.
		 */
		private CommandParameterType(String stringRepresentation, boolean nullable) {
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
		 * @param string The string representing the type of the command.
		 * @return The type of the command.
		 */
		public static CommandParameterType fromString(String string) {
			/*
			 * For network and processing brevity, the type names have been
			 * condensed down to single letters. We can therefore just switch
			 * on the first character in the string.
			 */
			
			char letter = string.charAt(0);
			switch(letter) {
			case 's': return STRING;
			case 'i': return INTEGER;
			case 'l': return LONG;
			case 'f': return FLOAT;
			case 'd': return DOUBLE;
			case 'b': return BOOLEAN;
			default: throw new IllegalArgumentException("Invalid type name: " + string);
			}
		}
	}
}
