import no.hvl.dowhile.core.*;
import no.hvl.dowhile.utility.FileTools;
import no.hvl.dowhile.utility.TrackTools;
import org.alternativevision.gpx.beans.GPX;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileManagerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    private OperationManager opManager;
    private FileManager fileManager;
    private File appFolder;
    private File rawFolder;
    private File processedFolder;
    //private OperationFolder mainOperationFolder;
    private Operation operation;
    private String operationName;
    private TrackInfo trackInfo;

    @Before
    public void before() throws IOException {
        opManager = new OperationManager();
        fileManager = new FileManager(opManager);
        operationName = "TestOp";
        trackInfo = new TrackInfo();
        operation = new Operation(operationName, 30, 11, 2016, 11, 56);

        appFolder = tempFolder.newFolder("TrackGrabberTest");
        fileManager.setAppFolder(appFolder);

        fileManager.setupMainOperationFolder(operation);

        rawFolder = fileManager.setupFolder(fileManager.getMainOperationFolder().getOperationFolder(), "Raw");
        processedFolder = fileManager.setupFolder(fileManager.getMainOperationFolder().getOperationFolder(), "Processed");
        fileManager.setRawFolder(rawFolder);
        fileManager.setProcessedFolder(processedFolder);
    }

    @Test
    public void tempFolderExists() {
        assertNotNull(tempFolder);
    }

    @Test
    public void operationFolderIsSetUp() {
        assertTrue(fileManager.getMainOperationFolder().getOperationFolder().exists());
    }

    @Test
    public void rawFolderIsSetUp() {
        assertTrue(rawFolder.exists());
    }

    @Test
    public void processedFolderIsSetUp() {
        assertTrue(processedFolder.exists());
    }

    @Test
    public void operationFileIsCreated() {
        fileManager.createOperationFile(operation, fileManager.getMainOperationFolder().getOperationFolder());
        File opFile = FileTools.getFile(fileManager.getMainOperationFolder().getOperationFolder(), operation.getName() + ".txt");
        assertNotNull(opFile);
    }

    @Test
    public void appFolderWithoutConfigGetsConfig() {
        fileManager.setupConfig(appFolder);
        File config = FileTools.getFile(appFolder, "config.txt");
        assertNotNull(config);
    }

    @Test
    public void existingOperationsAreLoaded() {
        fileManager.createOperationFile(operation, fileManager.getMainOperationFolder().getOperationFolder());
        List<Operation> operations = fileManager.loadExistingOperations();
        assertNotNull(operations.get(0));
    }

    @Test
    public void existingOperationsAreNotLoadedFromEmptyFolder() {
        fileManager.getMainOperationFolder().getOperationFolder().delete();
        List<Operation> operations = fileManager.loadExistingOperations();
        assertTrue(operations.isEmpty());
    }

    @Test
    public void operationFileIsUpdated() throws FileNotFoundException {
        fileManager.createOperationFile(operation, fileManager.getMainOperationFolder().getOperationFolder());
        operation.setStartTime(2014, 10, 21, 10, 34);
        fileManager.updateOperationFile(operation);
        File updatedOpFile = FileTools.getFile(fileManager.getMainOperationFolder().getOperationFolder(), operationName + ".txt");
        String updatedDateString = "# Starttid: 21-10/2014 10:34 CET";
        assertTrue(FileTools.txtFileContainsString(updatedOpFile, updatedDateString));
    }

    @Test
    public void trackPointsOfTwoSimilarFilesAreEqual() {
        GPX gpx1 = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        GPX gpx2 = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        fileManager.saveGpxFile(gpx1, trackInfo, "Filnavn", rawFolder);
        File[] rawFiles = rawFolder.listFiles();
        assertTrue(TrackTools.trackPointsAreEqual(rawFiles, TrackTools.getTrackFromGPXFile(gpx2)));
    }

    @Test
    public void trackPointsOfTwoDifferentFilesAreDifferent() {
        GPX gpx1 = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        GPX gpx2 = TrackTools.getGpxFromFile(new File("src/test/resources/testFile2.gpx"));
        fileManager.saveGpxFile(gpx1, trackInfo, "Filnavn", rawFolder);
        File[] rawFiles = rawFolder.listFiles();
        assertFalse(TrackTools.trackPointsAreEqual(rawFiles, TrackTools.getTrackFromGPXFile(gpx2)));
    }

    @Test
    public void alreadyImportedFileIsAlreadyImported() {
        GPX gpx = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        fileManager.saveRawGpxFileInFolders(gpx, "Filnavn");
        assertTrue(fileManager.fileAlreadyImported(gpx));
    }

    @Test
    public void rawGPXFileIsSaved() {
        GPX gpx = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        fileManager.saveRawGpxFileInFolders(gpx, "Filnavn");
        File savedRawFile = FileTools.getFile(rawFolder, "Filnavn");
        assertNotNull(savedRawFile);
    }

    @Test
    public void processedGPXFileIsSaved() {
        GPX gpx = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        fileManager.saveProcessedGpxFileInFolders(gpx, trackInfo, "Filnavn");
        File savedProcessedFile = FileTools.getFile(processedFolder, "Filnavn");
        assertNotNull(savedProcessedFile);
    }
}
