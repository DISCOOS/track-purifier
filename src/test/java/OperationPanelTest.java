import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.gui.Window;
import no.hvl.dowhile.utility.Messages;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public class OperationPanelTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private OperationManager operationManager;
    private Window window;
    private FrameFixture windowFixture;

    @BeforeClass
    public static void setupOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @Before
    public void setup() throws IOException {
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                operationManager = new OperationManager();
                window = new Window(operationManager);
                window.getOPERATION_MANAGER().setupLocalFolders(temporaryFolder.newFolder("TEST_C_FOLDER_" + System.currentTimeMillis()));
                windowFixture = new FrameFixture(window);
            }
        });
    }

    @Test
    public void errorMessageOnInvalidCharactersInOperationName() {
        windowFixture.button("newOperationButton").click();
        windowFixture.textBox("operationNameInput").enterText("TestWrong-.-");
        windowFixture.button("registerNewButton").click();
        windowFixture.label("errorMessageLabel").requireText(Messages.INVALID_OPERATION_NAME.get());
    }

    @Test
    public void newOperationFieldsIsVisibleAfterClick() {
        windowFixture.button("newOperationButton").click();
        windowFixture.label("operationNameLabel").requireVisible();
        windowFixture.textBox("operationNameInput").requireVisible();
        windowFixture.label("errorMessageLabel").requireVisible();
        windowFixture.label("operationDateLabel").requireVisible();
        windowFixture.button("registerNewButton").requireVisible();
        windowFixture.button("backButton").requireVisible();
    }

    @Test
    public void newOperationFieldsIsHiddenAfterBackButtonClick() {
        windowFixture.button("newOperationButton").click();
        windowFixture.button("backButton").click();
        windowFixture.label("operationNameLabel").requireNotVisible();
        windowFixture.textBox("operationNameInput").requireNotVisible();
        windowFixture.label("errorMessageLabel").requireNotVisible();
        windowFixture.label("operationDateLabel").requireNotVisible();
        windowFixture.button("registerNewButton").requireNotVisible();
    }

    /*@Test
    public void errorMessageWhenTryingToCreateExistingNameOnOperation() {
        windowFixture.button("newOperationButton").click();
        windowFixture.textBox("operationNameInput").enterText("Tester GUI på Voss");
        windowFixture.button("registerNewButton").click();
        windowFixture.label("errorMessageLabel").requireText(Messages.OPERATION_NAME_ALREADY_EXISTS.get());
    }*/

    @After
    public void tearDown() {
        windowFixture.cleanUp();
    }
}
