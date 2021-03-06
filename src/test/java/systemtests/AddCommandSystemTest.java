package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DEBT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DEBT_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.HANDPHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.HANDPHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.HOME_PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.HOME_PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INTEREST_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INTEREST_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DEADLINE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DEBT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_HANDPHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_HOME_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_INTEREST_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_OFFICE_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_POSTAL_CODE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.OFFICE_PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.OFFICE_PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.POSTAL_CODE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.POSTAL_CODE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEBT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEBT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_HANDPHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_HANDPHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_HOME_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_HOME_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INTEREST_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INTEREST_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OFFICE_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OFFICE_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_POSTAL_CODE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_POSTAL_CODE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.commons.core.ListObserver;
import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.NearbyCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Deadline;
import seedu.address.model.person.Debt;
import seedu.address.model.person.Email;
import seedu.address.model.person.Handphone;
import seedu.address.model.person.HomePhone;
import seedu.address.model.person.Interest;
import seedu.address.model.person.Name;
import seedu.address.model.person.OfficePhone;
import seedu.address.model.person.PostalCode;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void add() throws Exception {
        Model model = getModel();
        /* Case: add a person without tags to a non-empty address book, command with leading spaces and trailing spaces
         * -> added
         */
        ReadOnlyPerson toAdd = AMY;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_AMY + "  " + HANDPHONE_DESC_AMY + " "
                + HOME_PHONE_DESC_AMY + " " + OFFICE_PHONE_DESC_AMY + " "
                + EMAIL_DESC_AMY + "   " + ADDRESS_DESC_AMY + "  " + POSTAL_CODE_DESC_AMY + "   "
                + DEBT_DESC_AMY  + "   " + INTEREST_DESC_AMY + " " + DEADLINE_DESC_AMY + "  "
                + TAG_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addPerson(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a duplicate person -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY + INTEREST_DESC_AMY + DEADLINE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: add a duplicate person except with different tags -> rejected */
        // "friends" is an existing tag used in the default model, see TypicalPersons#ALICE
        // This test will fail is a new tag that is not in the model is used, see the bug documented in
        // AddressBook#addPerson(ReadOnlyPerson)
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY + INTEREST_DESC_AMY
                + DEADLINE_DESC_AMY + " " + PREFIX_TAG.getPrefix() + "friends";
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: add a person with all fields same as another person in the address book except name -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_BOB).withHandphone(VALID_HANDPHONE_AMY)
                .withHomePhone(VALID_HOME_PHONE_AMY).withOfficePhone(VALID_OFFICE_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withPostalCode(VALID_POSTAL_CODE_AMY).withDebt(VALID_DEBT_AMY)
                .withTotalDebt(VALID_DEBT_AMY).withInterest(VALID_INTEREST_AMY).withDeadline(VALID_DEADLINE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_BOB + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY
                + INTEREST_DESC_AMY + DEADLINE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a person with all fields same as another person in the address book except handphone -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withHandphone(VALID_HANDPHONE_BOB)
                .withHomePhone(VALID_HOME_PHONE_AMY).withOfficePhone(VALID_OFFICE_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withPostalCode(VALID_POSTAL_CODE_AMY).withDebt(VALID_DEBT_AMY)
                .withTotalDebt(VALID_DEBT_AMY).withInterest(VALID_INTEREST_AMY).withDeadline(VALID_DEADLINE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_BOB + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY
                + INTEREST_DESC_AMY + DEADLINE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a person with all fields same as another person in the address book except home phone -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withHandphone(VALID_HANDPHONE_AMY)
                .withHomePhone(VALID_HOME_PHONE_BOB).withOfficePhone(VALID_OFFICE_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withPostalCode(VALID_POSTAL_CODE_AMY).withDebt(VALID_DEBT_AMY)
                .withTotalDebt(VALID_DEBT_AMY).withInterest(VALID_INTEREST_AMY).withDeadline(VALID_DEADLINE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_BOB
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY
                + INTEREST_DESC_AMY + DEADLINE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a person with all fields same as another person in the address book except office phone -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withHandphone(VALID_HANDPHONE_AMY)
                .withHomePhone(VALID_HOME_PHONE_AMY).withOfficePhone(VALID_OFFICE_PHONE_BOB).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withPostalCode(VALID_POSTAL_CODE_AMY).withDebt(VALID_DEBT_AMY)
                .withTotalDebt(VALID_DEBT_AMY).withInterest(VALID_INTEREST_AMY).withDeadline(VALID_DEADLINE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_BOB + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY
                + INTEREST_DESC_AMY + DEADLINE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a person with all fields same as another person in the address book except email -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withHandphone(VALID_HANDPHONE_AMY)
                .withHomePhone(VALID_HOME_PHONE_AMY).withOfficePhone(VALID_OFFICE_PHONE_AMY).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_AMY).withPostalCode(VALID_POSTAL_CODE_AMY).withDebt(VALID_DEBT_AMY)
                .withTotalDebt(VALID_DEBT_AMY).withInterest(VALID_INTEREST_AMY).withDeadline(VALID_DEADLINE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_BOB + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY + INTEREST_DESC_AMY + DEADLINE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a person with all fields same as another person in the address book except address -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withHandphone(VALID_HANDPHONE_AMY)
                .withHomePhone(VALID_HOME_PHONE_AMY).withOfficePhone(VALID_OFFICE_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_BOB).withPostalCode(VALID_POSTAL_CODE_AMY).withDebt(VALID_DEBT_AMY)
                .withTotalDebt(VALID_DEBT_AMY).withInterest(VALID_INTEREST_AMY).withDeadline(VALID_DEADLINE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_BOB
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY + INTEREST_DESC_AMY + DEADLINE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a person with all fields same as another person in the address book except postal code -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withHandphone(VALID_HANDPHONE_AMY)
                .withHomePhone(VALID_HOME_PHONE_AMY).withOfficePhone(VALID_OFFICE_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withPostalCode(VALID_POSTAL_CODE_BOB).withDebt(VALID_DEBT_AMY)
                .withTotalDebt(VALID_DEBT_AMY).withInterest(VALID_INTEREST_AMY).withDeadline(VALID_DEADLINE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_BOB + DEBT_DESC_AMY + INTEREST_DESC_AMY + DEADLINE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a person with all fields same as another person in the address book except debt -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withHandphone(VALID_HANDPHONE_AMY)
                .withHomePhone(VALID_HOME_PHONE_AMY).withOfficePhone(VALID_OFFICE_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withPostalCode(VALID_POSTAL_CODE_AMY).withDebt(VALID_DEBT_BOB)
                .withTotalDebt(VALID_DEBT_BOB).withInterest(VALID_INTEREST_AMY).withDeadline(VALID_DEADLINE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_BOB + INTEREST_DESC_AMY + DEADLINE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a person with all fields same as another person in the address book except deadline -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withHandphone(VALID_HANDPHONE_AMY)
                .withHomePhone(VALID_HOME_PHONE_AMY).withOfficePhone(VALID_OFFICE_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withPostalCode(VALID_POSTAL_CODE_AMY).withDebt(VALID_DEBT_AMY)
                .withTotalDebt(VALID_DEBT_AMY).withInterest(VALID_INTEREST_AMY).withDeadline(VALID_DEADLINE_BOB)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY + INTEREST_DESC_AMY + DEADLINE_DESC_BOB + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a person with all fields same as another person in the address book except interest -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withHandphone(VALID_HANDPHONE_AMY)
                .withHomePhone(VALID_HOME_PHONE_AMY).withOfficePhone(VALID_OFFICE_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withPostalCode(VALID_POSTAL_CODE_AMY).withDebt(VALID_DEBT_AMY)
                .withTotalDebt(VALID_DEBT_AMY).withInterest(VALID_INTEREST_BOB).withDeadline(VALID_DEADLINE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY + INTEREST_DESC_BOB + DEADLINE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: filters the person list before adding -> added */
        executeCommand(FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER);
        assert ListObserver.getCurrentFilteredList().size()
                < getModel().getAddressBook().getPersonList().size();
        assertCommandSuccess(IDA);

        /* Case: add to empty address book -> added */
        executeCommand(ClearCommand.COMMAND_WORD);
        assert getModel().getAddressBook().getPersonList().size() == 0;
        assertCommandSuccess(ALICE);

        /* Case: add a person with tags, command with parameters in random order -> added */
        toAdd = BOB;
        command = AddCommand.COMMAND_WORD + TAG_DESC_FRIEND + HANDPHONE_DESC_BOB + ADDRESS_DESC_BOB + NAME_DESC_BOB
                + POSTAL_CODE_DESC_BOB + DEBT_DESC_BOB + INTEREST_DESC_BOB + DEADLINE_DESC_BOB + HOME_PHONE_DESC_BOB
                + TAG_DESC_HUSBAND + EMAIL_DESC_BOB + OFFICE_PHONE_DESC_BOB;
        assertCommandSuccess(command, toAdd);

        /* Case: selects first card in the person list, add a person -> added, card selection remains unchanged */
        executeCommand(SelectCommand.COMMAND_WORD + " 1");
        assert getPersonListPanel().isAnyCardSelected();
        assertCommandSuccess(CARL);

        /* Case: selects first card in the nearby list, add a person -> added, card selection remains unchanged */
        executeCommand(NearbyCommand.COMMAND_WORD + " 1");
        assert getPersonListPanel().isAnyCardSelected();
        assertCommandSuccess(DANIEL);

        /* Case: add a person, missing tags -> added */
        assertCommandSuccess(HOON);

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + POSTAL_CODE_DESC_AMY
                + DEBT_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing phone -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + POSTAL_CODE_DESC_AMY
                + DEBT_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing email -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + ADDRESS_DESC_AMY + POSTAL_CODE_DESC_AMY
                + DEBT_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing address -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + POSTAL_CODE_DESC_AMY
                + DEBT_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing postal code -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + DEBT_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing debt -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + PersonUtil.getPersonDetailsForAddCommand(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_NAME_DESC + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid handphone -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + INVALID_HANDPHONE_DESC + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY;
        assertCommandFailure(command, Handphone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid home phone -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + INVALID_HOME_PHONE_DESC
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY;
        assertCommandFailure(command, HomePhone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid office phone -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + INVALID_OFFICE_PHONE_DESC + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY;
        assertCommandFailure(command, OfficePhone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + INVALID_EMAIL_DESC + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY;
        assertCommandFailure(command, Email.MESSAGE_EMAIL_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + INVALID_ADDRESS_DESC
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY;
        assertCommandFailure(command, Address.MESSAGE_ADDRESS_CONSTRAINTS);

        /* Case: invalid postal code -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + INVALID_POSTAL_CODE_DESC + DEBT_DESC_AMY;
        assertCommandFailure(command, PostalCode.MESSAGE_POSTAL_CODE_CONSTRAINTS);

        /* Case: invalid debt -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + INVALID_DEBT_DESC;
        assertCommandFailure(command, Debt.MESSAGE_DEBT_CONSTRAINTS);

        /* Case: invalid interest -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY + INVALID_INTEREST_DESC;
        assertCommandFailure(command, Interest.MESSAGE_INTEREST_CONSTRAINTS);

        /* Case: invalid deadline -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY + INVALID_DEADLINE_DESC;
        assertCommandFailure(command, Deadline.MESSAGE_DEADLINE_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + HANDPHONE_DESC_AMY + HOME_PHONE_DESC_AMY
                + OFFICE_PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY + DEBT_DESC_AMY + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and verifies that the command box displays
     * an empty string, the result display box displays the success message of executing {@code AddCommand} with the
     * details of {@code toAdd}, and the model related components equal to the current model added with {@code toAdd}.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the command box has the default style class, the status bar's sync status changes,
     * the browser url and selected card remains unchanged.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(ReadOnlyPerson toAdd) {
        assertCommandSuccess(PersonUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(ReadOnlyPerson)}. Executes {@code command}
     * instead.
     * @see AddCommandSystemTest#assertCommandSuccess(ReadOnlyPerson)
     */
    private void assertCommandSuccess(String command, ReadOnlyPerson toAdd) {
        Model expectedModel = getModel();
        try {
            expectedModel.addPerson(toAdd);
        } catch (DuplicatePersonException dpe) {
            throw new IllegalArgumentException("toAdd already exists in the model.");
        }
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd.getName());

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, ReadOnlyPerson)} except that the result
     * display box displays {@code expectedResultMessage} and the model related components equal to
     * {@code expectedModel}.
     * @see AddCommandSystemTest#assertCommandSuccess(String, ReadOnlyPerson)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
