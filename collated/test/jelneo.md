# jelneo
###### \java\guitests\guihandles\MainWindowHandle.java
``` java
    /**
     * Logs into sample user account so that other GUI tests can test the main GUIs in the address book.
     */
    public static void simulateLogin() {
        try {
            Username username = new Username(TEST_USERNAME);
            Password password = new Password(TEST_PASSWORD);
            LoginCommand loginCommand = new LoginCommand(username, password);
            loginCommand.setData(modelManager, new CommandHistory(), new UndoRedoStack());
            loginCommand.execute();
        } catch (IllegalValueException ive) {
            ive.printStackTrace();
        } catch (CommandException ce) {
            ce.printStackTrace();
        }
    }
```
###### \java\seedu\address\logic\commands\BorrowCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code BorrowCommand}.
 */
public class BorrowCommandTest extends CommandTest {

    private static final String VALID_DEBT_FIGURE = "5000000.50";
    private static final String INVALID_DEBT_FIGURE = "-5000000.50";

    @Test
    public void execute_successfulBorrowing() {
        ReadOnlyPerson personWhoBorrowed = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String expectedMessage = ListObserver.MASTERLIST_NAME_DISPLAY_FORMAT
                + String.format(BorrowCommand.MESSAGE_BORROW_SUCCESS,
                personWhoBorrowed.getName().toString(), VALID_DEBT_FIGURE);
        try {
            Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
            expectedModel.addDebtToPerson(personWhoBorrowed, new Debt(VALID_DEBT_FIGURE));

            BorrowCommand borrowCommand = prepareCommand(INDEX_FIRST_PERSON, new Debt(VALID_DEBT_FIGURE));

            assertCommandSuccess(borrowCommand, model, expectedMessage, expectedModel);
        } catch (IllegalValueException | PersonNotFoundException | CommandException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void execute_unsuccessfulBorrowing() throws Exception {
        // Only case where borrowing fails is when debt amount is entered incorrectly
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(Debt.MESSAGE_DEBT_CONSTRAINTS);
        prepareCommand(INDEX_FIRST_PERSON, new Debt(INVALID_DEBT_FIGURE));
        fail(UNEXPECTED_EXECTION);
    }

```
###### \java\seedu\address\logic\commands\BorrowCommandTest.java
``` java
    @Test
    public void equals() throws Exception {
        BorrowCommand borrowFirstCommand = new BorrowCommand(INDEX_FIRST_PERSON, new Debt("50000"));
        BorrowCommand borrowSecondCommand = new BorrowCommand(INDEX_SECOND_PERSON, new Debt("20000"));

        // same object -> returns true
        assertTrue(borrowFirstCommand.equals(borrowFirstCommand));

        // same values -> returns true
        BorrowCommand borrowFirstCommandCopy = new BorrowCommand(INDEX_FIRST_PERSON, new Debt("50000"));
        assertTrue(borrowFirstCommand.equals(borrowFirstCommandCopy));

        // different types -> returns false
        assertFalse(borrowFirstCommand.equals(1));

        // null -> returns false
        assertFalse(borrowFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(borrowFirstCommand.equals(borrowSecondCommand));
    }

    /**
     * Returns a {@code BorrowCommand} with the parameter {@code index}.
     */
    private BorrowCommand prepareCommand(Index index, Debt debt) throws CommandException {
        BorrowCommand borrowCommand = new BorrowCommand(index, debt);
        borrowCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return borrowCommand;
    }

    /**
     * Returns a {@code BorrowCommand} with no parameters.
     */
    private BorrowCommand prepareCommand(Debt debt) throws CommandException {
        BorrowCommand borrowCommand = new BorrowCommand(debt);
        borrowCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return borrowCommand;
    }
}
```
###### \java\seedu\address\logic\commands\EditCommandTest.java
``` java
    @Test
    public void execute_totalDebtLessThanCurrentDebt_failure() throws Exception {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        descriptor.setTotalDebt(new Debt("0"));
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format("Total debt cannot be less than current debt");

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandFailure(editCommand, model, expectedMessage);
    }
```
###### \java\seedu\address\logic\commands\FilterCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest extends CommandTest {
    public static final String ONE_OR_MORE_SPACES_REGEX = "\\s+";
    public static final String[] SAMPLE_TAGS = "violent friendly".split(ONE_OR_MORE_SPACES_REGEX);
    public static final String[] SAMPLE_TAG = {"cooperative"};

    @Test
    public void equals() {
        List<String> firstTagList = Arrays.asList(SAMPLE_TAGS);
        List<String> secondTagList = Arrays.asList(SAMPLE_TAG);

        FilterCommand filterFirstCommand = new FilterCommand(firstTagList);
        FilterCommand filterSecondCommand = new FilterCommand(secondTagList);

        // same object
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values
        FilterCommand filterFirstCommandCopy = new FilterCommand(firstTagList);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different values
        assertFalse(filterFirstCommand.equals(filterSecondCommand));

        // different types
        assertFalse(filterFirstCommand.equals(1));

        // null vlaue
        assertFalse(filterFirstCommand.equals(null));
    }

    @Test
    public void execute_noTags_noPersonFound() {
        String expectedMessage =  String.format(MESSAGE_FILTER_ACKNOWLEDGEMENT, Arrays.asList(""),
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0));
        FilterCommand filterCommand = prepareCommand("      ");
        assertCommandSuccess(filterCommand, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_singleTag_noPersonFound() {
        String expectedMessage =  String.format(MESSAGE_FILTER_ACKNOWLEDGEMENT, Arrays.asList("safsaf2sf"),
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0));
        FilterCommand filterCommand = prepareCommand("safsaf2sf");
        assertCommandSuccess(filterCommand, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_singleTag_multiplePersonsFound() {
        String expectedMessage =  String.format(MESSAGE_FILTER_ACKNOWLEDGEMENT, Arrays.asList("violent"),
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2));
        FilterCommand filterCommand = prepareCommand("violent");
        assertCommandSuccess(filterCommand, expectedMessage, Arrays.asList(DANIEL, ELLE));
    }

    @Test
    public void execute_multipleTags_multiplePersonsFound() {
        String expectedMessage =  String.format(MESSAGE_FILTER_ACKNOWLEDGEMENT, Arrays.asList("friendly", "tricky"),
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 4));
        FilterCommand filterCommand = prepareCommand("friendly tricky");
        assertCommandSuccess(filterCommand, expectedMessage, Arrays.asList(ALICE, BENSON, DANIEL, FIONA));
    }

    /**
     * Parses {@code userInput} into a {@code FilterCommand}.
     */
    private FilterCommand prepareCommand(String userInput) {
        FilterCommand command =
                new FilterCommand(Arrays.asList(userInput.split(ONE_OR_MORE_SPACES_REGEX)));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<ReadOnlyPerson>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FilterCommand command, String expectedMessage,
                                      List<ReadOnlyPerson> expectedList) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getAddressBook());
    }
}
```
###### \java\seedu\address\logic\commands\LoginCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code LoginCommand}.
 */
public class LoginCommandTest extends CommandTest {
    private static final String TEST_USERNAME = "TESTloanShark97";
    private static final String TEST_PASSWORD = "TESThitMeUp123";
    private final boolean hasLoggedIn = true;
    private LoginAppRequestEvent event;
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_login_success() {
        try {
            LoginCommand loginCommand = prepareCommand(TEST_USERNAME, TEST_PASSWORD);
            CommandResult result = loginCommand.execute();
            assertEquals(hasLoggedIn, event.getLoginStatus());
            assertEquals(MESSAGE_LOGIN_ACKNOWLEDGEMENT, result.feedbackToUser);
        } catch (CommandException ce) {
            ce.printStackTrace();
        }
    }

    @Test
    public void execute_login_failure() {
        try {
            LoginCommand loginCommand = prepareCommand(TEST_USERNAME, TEST_PASSWORD);
            CommandResult result = loginCommand.execute();
            assertEquals(!hasLoggedIn, event.getLoginStatus());
            assertEquals(MESSAGE_LOGIN_UNSUCCESSFUL, result.feedbackToUser);
        } catch (CommandException ce) {
            ce.printStackTrace();
        }
    }

    @Test
    public void equals() {
        try {
            Username usernameOne = new Username("hellonihao123");
            Username usernameTwo = new Username("nihaohello123");
            Password passwordOne = new Password("idontcare!?");
            Password passwordTwo = new Password("careidont!?");

            LoginCommand loginFirstCommand = new LoginCommand(usernameOne, passwordOne);
            LoginCommand loginSecondCommand = new LoginCommand(usernameTwo, passwordTwo);

            // same object -> returns true
            assertTrue(loginFirstCommand.equals(loginFirstCommand));

            // same values -> returns true
            LoginCommand loginFirstCommandCopy = new LoginCommand(usernameOne, passwordOne);
            assertTrue(loginFirstCommand.equals(loginFirstCommandCopy));

            // different types -> returns false
            assertFalse(loginFirstCommand.equals(1));

            // null -> returns false
            assertFalse(loginFirstCommand.equals(null));

            // different login details -> returns false
            assertFalse(loginFirstCommand.equals(loginSecondCommand));
        } catch (IllegalValueException ive) {
            ive.printStackTrace();
        }
    }

    /**
     * Parses {@code usernameInput} and {@passwordInput} into a {@code LoginCommand}.
     */
    private LoginCommand prepareCommand(String usernameInput, String passwordInput) {
        LoginCommand command = null;
        try {
            Username username = new Username(usernameInput);
            Password password = new Password(passwordInput);
            command = new LoginCommand(username, password);
            command.setData(model, new CommandHistory(), new UndoRedoStack());
        } catch (IllegalValueException ive) {
            ive.printStackTrace();
        }
        return command;
    }
}
```
###### \java\seedu\address\logic\commands\LogoutCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code LogoutCommand}.
 */
public class LogoutCommandTest extends CommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_logout_success() {
        LogoutCommand logoutCommand = new LogoutCommand();
        logoutCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        CommandResult result = logoutCommand.execute();
        assertEquals(MESSAGE_LOGOUT_ACKNOWLEDGEMENT, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof LogoutAppRequestEvent);
    }
}
```
###### \java\seedu\address\logic\commands\PaybackCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code PaybackCommand}.
 */
public class PaybackCommandTest extends CommandTest {

    private static final String VALID_DEBT_FIGURE = "5000000.50";
    private static final String INVALID_DEBT_FIGURE = "-5000000.50";
    private static final String ENORMOUS_DEBT_FIGURE = "50000000000000000000.50";
    private static final String MESSAGE_INVALID_FORMAT =  String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            PaybackCommand.MESSAGE_USAGE);

    @Test
    public void execute_payback_success() {
        ReadOnlyPerson personWhoPayback = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String expectedMessage = ListObserver.MASTERLIST_NAME_DISPLAY_FORMAT
                + String.format(PaybackCommand.MESSAGE_PAYBACK_SUCCESS,
                personWhoPayback.getName().toString(), VALID_DEBT_FIGURE);
        try {
            Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
            expectedModel.deductDebtFromPerson(personWhoPayback, new Debt(VALID_DEBT_FIGURE));

            PaybackCommand paybackCommand = prepareCommand(INDEX_FIRST_PERSON, new Debt(VALID_DEBT_FIGURE));
            assertCommandSuccess(paybackCommand, model, expectedMessage, expectedModel);
        } catch (IllegalValueException | PersonNotFoundException | CommandException e) {
            e.printStackTrace();
        }
    }

```
###### \java\seedu\address\logic\commands\PaybackCommandTest.java
``` java
    @Test
    public void execute_invalidDebtFigure_failure() throws Exception {
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(Debt.MESSAGE_DEBT_CONSTRAINTS);
        prepareCommand(INDEX_FIRST_PERSON, new Debt(INVALID_DEBT_FIGURE));
        fail(UNEXPECTED_EXECTION);
    }

    @Test
    public void execute_amountExceedDebtOwed_failure() {
        try {
            PaybackCommand paybackCommand = prepareCommand(INDEX_FIRST_PERSON, new Debt(ENORMOUS_DEBT_FIGURE));
            assertCommandFailure(paybackCommand, model, PaybackCommand.MESSAGE_PAYBACK_FAILURE);
        } catch (IllegalValueException | CommandException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void equals() throws Exception {
        PaybackCommand paybackFirstCommand = new PaybackCommand(INDEX_FIRST_PERSON, new Debt("500"));
        PaybackCommand paybackSecondCommand = new PaybackCommand(INDEX_SECOND_PERSON, new Debt("300"));

        // same object -> returns true
        assertTrue(paybackFirstCommand.equals(paybackFirstCommand));

        // same values -> returns true
        PaybackCommand paybackFirstCommandCopy = new PaybackCommand(INDEX_FIRST_PERSON, new Debt("500"));
        assertTrue(paybackFirstCommand.equals(paybackFirstCommandCopy));

        // different types -> returns false
        assertFalse(paybackFirstCommand.equals(1));

        // null -> returns false
        assertFalse(paybackFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(paybackFirstCommand.equals(paybackSecondCommand));
    }

    /**
     * Prepares a {@code PaybackCommand}.
     */
    private PaybackCommand prepareCommand(Index index, Debt debt) throws CommandException {
        PaybackCommand command = null;
        if (index == null) {
            command = new PaybackCommand(debt);
        } else {
            command = new PaybackCommand(index, debt);
        }
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\parser\BorrowCommandParserTest.java
``` java
public class BorrowCommandParserTest extends CommandTest {

    private static final String MESSAGE_INVALID_FORMAT =  String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            BorrowCommand.MESSAGE_USAGE);
    private static final String VALID_DEBT_FIGURE = "500.20";
    private static final String INVALID_DEBT_FIGURE = "-500";
    private static final String VALID_INDEX = "1";
    private static final String INVALID_INDEX = "-1";

    private BorrowCommandParser parser  = new BorrowCommandParser();

    @Test
    public void parse_missingParts_failure() throws Exception {
        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidArguments() throws Exception {
        // invalid index and amount
        assertParseFailure(parser, INVALID_INDEX + " " + INVALID_DEBT_FIGURE, MESSAGE_INVALID_FORMAT);

        // valid index but invalid amount
        assertParseFailure(parser, VALID_INDEX + " " + INVALID_DEBT_FIGURE, MESSAGE_INVALID_FORMAT);

        // invalid index but valid amount
        assertParseFailure(parser, INVALID_INDEX + " " + VALID_DEBT_FIGURE, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidNumberOfArguments_failure() {
        assertParseFailure(parser, "1 200 300", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validArguments() {
        try {
            Index index = Index.fromOneBased(Integer.valueOf(VALID_INDEX));
            Debt amount = new Debt(VALID_DEBT_FIGURE);
            BorrowCommand expectedBorrowCommand = new BorrowCommand(index, amount);
            assertParseSuccess(parser, VALID_INDEX + " " + VALID_DEBT_FIGURE, expectedBorrowCommand);

            amount = new Debt(VALID_DEBT_FIGURE);
            expectedBorrowCommand = new BorrowCommand(amount);
            assertParseSuccess(parser, VALID_DEBT_FIGURE, expectedBorrowCommand);
        } catch (IllegalValueException | CommandException e) {
            e.printStackTrace();
        }
    }
}
```
###### \java\seedu\address\logic\parser\FilterCommandParserTest.java
``` java
public class FilterCommandParserTest {
    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() throws Exception {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() throws Exception {
        // no leading and trailing whitespaces
        FilterCommand expectedFilterCommand = new FilterCommand(Arrays.asList("violent", "friendly"));
        assertParseSuccess(parser, "violent friendly", expectedFilterCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n violent \n \t friendly  \t", expectedFilterCommand);

        // alphanumeric arguments
        expectedFilterCommand = new FilterCommand(Arrays.asList("violent12", "friendly435", "2141244"));
        assertParseSuccess(parser, " violent12 friendly435 2141244", expectedFilterCommand);
    }

    @Test
    public void parse_invalidArgs() {
        // no arguments
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);

        // invalid tag names
        assertParseFailure(parser, "???*^%%", MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "???*^%%sfsa !abc", MESSAGE_TAG_CONSTRAINTS);
    }
}
```
###### \java\seedu\address\logic\parser\LoginCommandParserTest.java
``` java
public class LoginCommandParserTest {
    private static final String VALID_USERNAME = "User_1234";
    private static final String VALID_PASSWORD = "P@$$worD";
    private static final String GUI_LOGIN_ARGS = " " + "%1$s" + SEPARATOR + "%2$s" + SEPARATOR;

    private LoginCommandParser parser = new LoginCommandParser();

    @Test
    public void cliParse_validArgs_returnsLoginCommand() {
        setShowingLoginView(false);

        try {
            LoginCommand expectedLoginCommand =
                    new LoginCommand(new Username(VALID_USERNAME), new Password(VALID_PASSWORD));

            //no leading and trailing whitespaces
            assertParseSuccess(parser, VALID_USERNAME + " " + VALID_PASSWORD, expectedLoginCommand);

            // with leading and trailing whitespaces
            assertParseSuccess(parser, VALID_USERNAME + " " + VALID_PASSWORD + " ", expectedLoginCommand);
        } catch (IllegalValueException ive) {
            ive.printStackTrace();
        }
    }

    @Test
    public void cliParse_invalidArgs() {
        setShowingLoginView(false);
        // empty input
        assertParseFailure(parser, "    ", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, LoginCommand.MESSAGE_USAGE));

        // invalid username
        assertParseFailure(parser, "%&^%&$bhh" + " " + TEST_PASSWORD, MESSAGE_USERNAME_CHARACTERS_CONSTRAINTS);

        // invalid password
        assertParseFailure(parser, TEST_USERNAME + " " + "123", MESSAGE_PASSWORD_LENGTH_CONSTRAINTS);
    }

    @Test
    public void guiParse_validArgs_returnsLoginCommand() {
        setShowingLoginView(true);

        try {
            LoginCommand expectedLoginCommand =
                    new LoginCommand(new Username(VALID_USERNAME), new Password(VALID_PASSWORD));

            assertParseSuccess(parser,
                    String.format(GUI_LOGIN_ARGS, VALID_USERNAME, VALID_PASSWORD), expectedLoginCommand);
        } catch (IllegalValueException ive) {
            ive.printStackTrace();
        }
    }

    @Test
    public void guiParse_invalidArgs() {
        setShowingLoginView(true);

        // no username
        assertParseFailure(parser, String.format(GUI_LOGIN_ARGS, "", TEST_PASSWORD), EMPTY_USERNAME_MESSAGE);

        // no password
        assertParseFailure(parser, String.format(GUI_LOGIN_ARGS, TEST_USERNAME, ""), EMPTY_PASSWORD_MESSAGE);

        // no username and password
        assertParseFailure(parser, String.format(GUI_LOGIN_ARGS, "", ""), EMPTY_USERNAME_MESSAGE);
    }
}
```
###### \java\seedu\address\logic\parser\PaybackCommandParserTest.java
``` java
public class PaybackCommandParserTest extends CommandTest {

    private static final String MESSAGE_INVALID_FORMAT =  String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            PaybackCommand.MESSAGE_USAGE);
    private static final String VALID_DEBT_FIGURE = "500.20";
    private static final String INVALID_DEBT_FIGURE = "-500";
    private static final String VALID_INDEX = "1";
    private static final String INVALID_INDEX = "-1";

    private PaybackCommandParser parser  = new PaybackCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidNumberOfArguments_failure() {
        assertParseFailure(parser, "1 200 300", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidArguments() {
        // invalid index and amount
        assertParseFailure(parser, INVALID_INDEX + " " + INVALID_DEBT_FIGURE, MESSAGE_INVALID_FORMAT);

        // valid index but invalid amount
        assertParseFailure(parser, VALID_INDEX + " " + INVALID_DEBT_FIGURE, MESSAGE_INVALID_FORMAT);

        // invalid index but valid amount
        assertParseFailure(parser, INVALID_INDEX + " " + VALID_DEBT_FIGURE, MESSAGE_INVALID_FORMAT);

        // without index but invalid amount
        assertParseFailure(parser, " " + INVALID_DEBT_FIGURE, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validArguments() throws Exception {
        try {
            //valid arguments with index
            Index index = Index.fromOneBased(Integer.valueOf(VALID_INDEX));
            Debt amount = new Debt(VALID_DEBT_FIGURE);
            PaybackCommand expectedPaybackCommand = new PaybackCommand(index, amount);
            assertParseSuccess(parser, VALID_INDEX + " " + VALID_DEBT_FIGURE, expectedPaybackCommand);

            // valid arguments without index
            amount = new Debt(VALID_DEBT_FIGURE);
            expectedPaybackCommand = new PaybackCommand(amount);
            assertParseSuccess(parser, " " + VALID_DEBT_FIGURE, expectedPaybackCommand);
        } catch (IllegalValueException | CommandException e) {
            e.printStackTrace();
        }
    }
}
```
###### \java\seedu\address\logic\PasswordTest.java
``` java
public class PasswordTest {

    @Test
    public void isValidPasswordLength() {
        // password does not meet length requirements
        assertFalse(Password.isValidPasswordLength(""));
        assertFalse(Password.isValidPasswordLength("_"));
        assertFalse(Password.isValidPasswordLength("pwd_1"));

        // password meets length requirement
        assertTrue(Password.isValidPasswordLength("abcde12345"));
        assertTrue(Password.isValidPasswordLength("ABCE1234!@#"));
    }

    @Test
    public void hasValidPasswordCharacters() {
        // blank password
        assertFalse(Password.isValidPasswordLength(""));
        assertFalse(Password.isValidPasswordLength(" "));

        // password contains illegal characters
        assertFalse(Password.hasValidPasswordCharacters("?what?"));
        assertFalse(Password.hasValidPasswordCharacters("?what?The//"));
        assertFalse(Password.hasValidPasswordCharacters("?what?The][]"));

        // valid password
        assertTrue(Password.hasValidPasswordCharacters("password123"));
        assertTrue(Password.hasValidPasswordCharacters("password_-#$"));
        assertTrue(Password.hasValidPasswordCharacters("PASSword_-#$"));
    }

}
```
###### \java\seedu\address\logic\UsernameTest.java
``` java
public class UsernameTest {

    @Test
    public void hasValidUsernameCharacters() {
        // blank username
        assertFalse(Username.hasValidUsernameCharacters(""));
        assertFalse(Username.hasValidUsernameCharacters(" "));

        // username that contains illegal characters
        assertFalse(Username.hasValidUsernameCharacters("?username?"));
        assertFalse(Username.hasValidUsernameCharacters(".username123.%@$#"));

        // valid username
        assertTrue(Username.hasValidUsernameCharacters("username123"));
        assertTrue(Username.hasValidUsernameCharacters("UsErName123"));
        assertTrue(Username.hasValidUsernameCharacters("UsErName123__"));
    }

    @Test
    public void isValidUsernameLength() {
        // username that is less than length requirement
        assertFalse(Username.isValidUsernameLength(""));
        assertFalse(Username.isValidUsernameLength("1"));
        assertFalse(Username.isValidUsernameLength("Abc1_"));

        // username that meets the length requirement
        assertTrue(Username.isValidUsernameLength("123456"));
        assertTrue(Username.isValidUsernameLength("aBc123_oi"));
    }
}
```
###### \java\seedu\address\testutil\PersonBuilder.java
``` java
    /**
     * Sets the {@code totalDebt} of the {@code Person} that we are building.
     */
    public PersonBuilder withTotalDebt(String debt) {
        try {
            this.person.setTotalDebt(new Debt(debt));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("debt is expected to be unique.");
        }
        return this;
    }
```
###### \java\seedu\address\ui\CommandBoxTest.java
``` java
    /**
     * Logs into sample user account so that other GUI tests can test the main GUIs in the address book.
     */
    public void simulateLogin() {
        try {
            Username username = new Username(TEST_USERNAME);
            Password password = new Password(TEST_PASSWORD);
            LoginCommand loginCommand = new LoginCommand(username, password);
            loginCommand.setData(model, new CommandHistory(), new UndoRedoStack());
            loginCommand.execute();
        } catch (IllegalValueException ive) {
            ive.printStackTrace();
        } catch (CommandException ce) {
            ce.printStackTrace();
        }
    }
```
###### \java\seedu\address\ui\PreLoginCommandBoxTest.java
``` java
public class PreLoginCommandBoxTest extends GuiUnitTest {

    private static final String COMMAND_THAT_IS_NOT_RECOGNIZED = "sfewgrw43";

    private ArrayList<String> defaultStyleOfCommandBox;
    private ArrayList<String> errorStyleOfCommandBox;

    private CommandBoxHandle commandBoxHandle;
    private Model model;
    private ModelManager modelManager;
    private String adminUsername;
    private String adminPassword;

    @Before
    public void setUp() {
        model = new ModelManager();
        modelManager = new ModelManager();
        ListObserver.init(model);

        Logic logic = new LogicManager(model);
        adminUsername = TEST_USERNAME;
        adminPassword = TEST_PASSWORD;
        CommandBox commandBox = new CommandBox(logic);
        commandBoxHandle = new CommandBoxHandle(getChildNode(commandBox.getRoot(),
                CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        uiPartRule.setUiPart(commandBox);

        defaultStyleOfCommandBox = new ArrayList<>(commandBoxHandle.getStyleClass());

        errorStyleOfCommandBox = new ArrayList<>(defaultStyleOfCommandBox);
        errorStyleOfCommandBox.add(CommandBox.ERROR_STYLE_CLASS);
    }

    @Test
    public void commandBox_successfulCommandInputs() {
        // permitted inputs that contain the command keywords: exit, help, login
        commandBoxHandle.run(ExitCommand.COMMAND_WORD);
        assertBehaviorForSuccessfulCommand(ExitCommand.COMMAND_WORD);

        commandBoxHandle.run(LoginCommand.COMMAND_WORD);
        assertBehaviorForSuccessfulCommand(LoginCommand.COMMAND_WORD);

        commandBoxHandle.run(HelpCommand.COMMAND_WORD);
        assertBehaviorForSuccessfulCommand(HelpCommand.COMMAND_WORD);
    }

    @Test
    public void commandBox_unsuccessfulCommandInputs() {
        // recognised commands which are not login, help or exit
        commandBoxHandle.run(ListCommand.COMMAND_WORD);
        assertBehaviorForFailedCommand(ListCommand.COMMAND_WORD);

        commandBoxHandle.run(ClearCommand.COMMAND_WORD);
        assertBehaviorForFailedCommand(ClearCommand.COMMAND_WORD);

        commandBoxHandle.run(AddCommand.COMMAND_WORD);
        assertBehaviorForFailedCommand(AddCommand.COMMAND_WORD);

        commandBoxHandle.run(DeleteCommand.COMMAND_WORD);
        assertBehaviorForFailedCommand(DeleteCommand.COMMAND_WORD);

        commandBoxHandle.run(EditCommand.COMMAND_WORD);
        assertBehaviorForFailedCommand(EditCommand.COMMAND_WORD);

        commandBoxHandle.run(FindCommand.COMMAND_WORD);
        assertBehaviorForFailedCommand(FindCommand.COMMAND_WORD);

        commandBoxHandle.run(HistoryCommand.COMMAND_WORD);
        assertBehaviorForFailedCommand(HistoryCommand.COMMAND_WORD);

        commandBoxHandle.run(RedoCommand.COMMAND_WORD);
        assertBehaviorForFailedCommand(RedoCommand.COMMAND_WORD);

        commandBoxHandle.run(SelectCommand.COMMAND_WORD);
        assertBehaviorForFailedCommand(SelectCommand.COMMAND_WORD);

        commandBoxHandle.run(UndoCommand.COMMAND_WORD);
        assertBehaviorForFailedCommand(UndoCommand.COMMAND_WORD);

        commandBoxHandle.run(NearbyCommand.COMMAND_WORD);
        assertBehaviorForFailedCommand(NearbyCommand.COMMAND_WORD);

        // unrecognised commands
        commandBoxHandle.run(COMMAND_THAT_IS_NOT_RECOGNIZED);
        assertBehaviorForFailedCommand(COMMAND_THAT_IS_NOT_RECOGNIZED);
    }

    /**
     * Runs a command that fails, then verifies that <br>
     *      - the text remains <br>
     *      - the command box's style is the same as {@code errorStyleOfCommandBox}.
     */
    private void assertBehaviorForFailedCommand(String commandKeyword) {
        commandBoxHandle.run(commandKeyword);
        assertEquals(commandKeyword, commandBoxHandle.getInput());
        assertEquals(errorStyleOfCommandBox, commandBoxHandle.getStyleClass());
    }

    /**
     * Runs a command that succeeds, then verifies that <br>
     *      - the text is cleared <br>
     *      - the command box's style is the same as {@code defaultStyleOfCommandBox}.
     */
    private void assertBehaviorForSuccessfulCommand(String commandKeyword) {
        commandBoxHandle.run(commandKeyword);
        assertEquals("", commandBoxHandle.getInput());
        assertEquals(defaultStyleOfCommandBox, commandBoxHandle.getStyleClass());
    }
}
```
