package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RepaidCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RepaidCommand object
 */
public class RepaidCommandParser {
    /**
    * Parses the given {@code String} of arguments in the context of the RepaidCommand
    * and returns an RepaidCommand object for execution.
    * @throws ParseException if the user input does not conform the expected format
    */
    public RepaidCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new RepaidCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RepaidCommand.MESSAGE_USAGE));
        }
    }

}
