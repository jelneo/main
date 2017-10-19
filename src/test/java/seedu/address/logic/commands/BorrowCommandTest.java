package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Debt;
import seedu.address.model.person.ReadOnlyPerson;

//@@author jelneo
/**
 * Contains integration tests (interaction with the Model) and unit tests for BorrowCommand.
 */
public class BorrowCommandTest {

    private static final String VALID_DEBT_FIGURE = "5000000.50";
    private static final String INVALID_DEBT_FIGURE = "-5000000.50";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_successfulBorrowing() {
        Index firstPerson = Index.fromOneBased(1);
        ReadOnlyPerson personWhoBorrowed = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        try {
            Debt debtAmount = new Debt(VALID_DEBT_FIGURE);
            Debt expectedDebt = personWhoBorrowed.getDebt();
            expectedDebt.addToDebt(debtAmount);

            String expectedMessage = String.format(BorrowCommand.MESSAGE_BORROW_SUCCESS,
                    personWhoBorrowed.getName().toString(), debtAmount);
            BorrowCommand borrowCommand = new BorrowCommand(firstPerson, debtAmount);
            Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

            borrowCommand.setData(expectedModel, new CommandHistory(), new UndoRedoStack());
            assertCommandSuccess(borrowCommand, model, expectedMessage, expectedModel);
        } catch (IllegalValueException ive) {
            ive.printStackTrace();
        }
    }



    @Test
    public void execute_unsuccessfulBorrowing() throws IllegalValueException {
        // Only case where borrowing fails is when debt amount is entered incorrectly
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(Debt.MESSAGE_DEBT_CONSTRAINTS);

        Debt debtAmount = new Debt(INVALID_DEBT_FIGURE);
    }
}