package no.hvl.dowhile.core.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;
import no.hvl.dowhile.utility.StringTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

/**
 * This class has an interface for creating a new operation or choosing an existing operation.
 */
public class OperationPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private JLabel operationNameLabel;
    private JTextField operationNameInput;
    private JLabel operationDateLabel;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private JLabel awaitingGPSLabel;
    private JLabel errorMessageLabel;

    private JLabel existingOperationLabel;
    private JComboBox<String> existingOperationInput;
    private JButton registerNewButton;
    private JButton registerExistingButton;

    private JButton backButton;

    private JButton newOperationButton;
    private JButton existingOperationButton;

    private JButton importFileButton;
    private JButton switchOperationButton;
    private JButton toggleEditInfoButton;
    private JLabel editDateLabel;
    private DatePicker editDatePicker;
    private TimePicker editTimePicker;
    private JButton saveOperationButton;

    private GridBagConstraints constraints;

    public OperationPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;

        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        WINDOW.setConstraintsInsets(constraints, 5);

        // New operation label
        operationNameLabel = WINDOW.makeLabel(Messages.OPERATION_NAME.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.WEST, 2);
        add(operationNameLabel, constraints);

        // Already existing operation label
        existingOperationLabel = WINDOW.makeLabel(Messages.EXISTING_OPERATION.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.CENTER, 2);
        add(existingOperationLabel, constraints);

        // New operation button
        newOperationButton = new JButton(Messages.NEW_OPERATION_BUTTON.get());
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.CENTER, 2);
        add(newOperationButton, constraints);

        // New operation name input
        operationNameInput = new JTextField();
        WINDOW.modifyConstraints(constraints, 0, 3, GridBagConstraints.CENTER, 4);
        constraints.fill = GridBagConstraints.BOTH;
        add(operationNameInput, constraints);

        // Already existing operation input
        existingOperationInput = new JComboBox<>();
        WINDOW.modifyConstraints(constraints, 0, 3, GridBagConstraints.WEST, 2);
        add(existingOperationInput, constraints);

        // Import local GPX-file button
        importFileButton = new JButton(Messages.IMPORT_LOCAL_FILE.get());
        WINDOW.modifyConstraints(constraints, 0, 3, GridBagConstraints.CENTER, 4);
        add(importFileButton, constraints);

        // Edit info toggle button
        toggleEditInfoButton = new JButton(Messages.EDIT_INFO_SHOW_BUTTON.get());
        WINDOW.modifyConstraints(constraints, 0, 4, GridBagConstraints.CENTER, 2);
        add(toggleEditInfoButton, constraints);

        // Switch operation
        switchOperationButton = new JButton(Messages.CHOOSE_OTHER_OPERATION.get());
        WINDOW.modifyConstraints(constraints, 2, 4, GridBagConstraints.CENTER, 2);
        add(switchOperationButton, constraints);

        // Date for operation and input
        operationDateLabel = WINDOW.makeLabel(Messages.OPERATION_START_DATE.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 0, 4, GridBagConstraints.CENTER, 2);
        add(operationDateLabel, constraints);

        // Edit operation date label
        editDateLabel = WINDOW.makeLabel(Messages.EDIT_OPERATION_TIME.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 0, 4, GridBagConstraints.CENTER, 4);
        add(editDateLabel, constraints);

        datePicker = new DatePicker(createDateSettings());
        WINDOW.modifyConstraints(constraints, 0, 5, GridBagConstraints.CENTER, 2);
        add(datePicker, constraints);

        // Edit date of operation
        editDatePicker = new DatePicker(createDateSettings());
        WINDOW.modifyConstraints(constraints, 0, 5, GridBagConstraints.CENTER, 4);
        add(editDatePicker, constraints);

        // Edit time of operation
        editTimePicker = new TimePicker(createTimeSettings());
        WINDOW.modifyConstraints(constraints, 0, 6, GridBagConstraints.CENTER, 4);
        add(editTimePicker, constraints);

        // Save edited operation button
        saveOperationButton = new JButton(Messages.EDIT_OPERATION_BUTTON.get());
        WINDOW.modifyConstraints(constraints, 0, 7, GridBagConstraints.CENTER, 4);
        add(saveOperationButton, constraints);

        // Back button
        backButton = new JButton(Messages.GO_BACK.get());
        WINDOW.modifyConstraints(constraints, 0, 8, GridBagConstraints.WEST, 1);
        add(backButton, constraints);

        // Awaiting GPS label
        awaitingGPSLabel = WINDOW.makeLabel(Messages.AWAITING_GPS.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 1, 0, GridBagConstraints.CENTER, 2);
        add(awaitingGPSLabel, constraints);
        awaitingGPSLabel.setVisible(false);

        // Existing operation button
        existingOperationButton = new JButton(Messages.EXISTING_OPERATION_BUTTON.get());
        WINDOW.modifyConstraints(constraints, 2, 2, GridBagConstraints.CENTER, 2);
        add(existingOperationButton, constraints);

        // Error message label
        errorMessageLabel = WINDOW.makeLabel(" ", WINDOW.TEXT_FONT_SIZE);
        errorMessageLabel.setForeground(Color.RED);
        WINDOW.modifyConstraints(constraints, 2, 3, GridBagConstraints.CENTER, 2);
        add(errorMessageLabel, constraints);
        errorMessageLabel.setVisible(false);

        // Register existing operation
        registerExistingButton = new JButton(Messages.REGISTER_BUTTON.get());
        WINDOW.modifyConstraints(constraints, 2, 3, GridBagConstraints.CENTER, 2);
        add(registerExistingButton, constraints);

        timePicker = new TimePicker(createTimeSettings());
        WINDOW.modifyConstraints(constraints, 2, 5, GridBagConstraints.WEST, 2);
        add(timePicker, constraints);

        // Register new operation
        registerNewButton = new JButton(Messages.REGISTER_BUTTON.get());
        WINDOW.modifyConstraints(constraints, 2, 6, GridBagConstraints.CENTER, 2);
        add(registerNewButton, constraints);

        backButton.setVisible(false);
        setVisibilityNewOperation(false);
        setVisibilityExistingOperation(false);
        setVisibilityEditInfo(false);
        setVisibilityToggleEditInfo(false);

        newOperationButtonListener();
        existingOperationButtonListener();
        registerExistingOperationButtonListener();
        registerNewOperationButtonListener();
        toggleEditInfoButtonListener();
        saveOperationButtonListener();
        switchOperationListener();
        backButtonListener();
        importFileButtonListener();

    }

    /**
     * Creating a settings object to use when creating a date picker.
     *
     * @return settings for a date picker.
     */
    private DatePickerSettings createDateSettings() {
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        dateSettings.setAllowEmptyDates(false);
        return dateSettings;
    }

    /**
     * Creating a settings object to use when creating a time picker.
     * @return settings for a time picker.
     */
    private TimePickerSettings createTimeSettings() {
        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.initialTime = LocalTime.now();
        return timeSettings;
    }

    /**
     * Updating the label with info about the operation.
     *
     * @param operation the current operation.
     */
    public void updateOperationInfo(Operation operation) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(operation.getStartTime());
        editDatePicker.setDate(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        editTimePicker.setTime(LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
    }

    /**
     * Adding the existing operations to selector.
     *
     * @param operations the operations to add.
     */
    public void addExistingOperations(List<Operation> operations) {
        for (Operation operation : operations) {
            existingOperationInput.addItem(operation.getName());
        }
    }

    /**
     * Set the visibility of the items required to make a new operation.
     *
     * @param visibility the new visibility.
     */
    private void setVisibilityNewOperation(boolean visibility) {
        operationNameInput.setVisible(visibility);
        operationDateLabel.setVisible(visibility);
        operationNameLabel.setVisible(visibility);
        datePicker.setVisible(visibility);
        timePicker.setVisible(visibility);
        registerNewButton.setVisible(visibility);
        backButton.setVisible(visibility);
    }

    /**
     * Set the visibility of the items required to find an existing operation.
     *
     * @param visibility the new visibility.
     */
    private void setVisibilityExistingOperation(boolean visibility) {
        existingOperationLabel.setVisible(visibility);
        existingOperationInput.setVisible(visibility);
        registerExistingButton.setVisible(visibility);
        backButton.setVisible(visibility);
    }

    /**
     * Set the visibility of the main operation selector button.
     *
     * @param visibility the new visibility.
     */
    private void setVisibilityOperationButtons(boolean visibility) {
        newOperationButton.setVisible(visibility);
        existingOperationButton.setVisible(visibility);
    }

    /**
     * Set the visibility of the items required to edit data about an operation.
     *
     * @param visibility the new visibility.
     */
    private void setVisibilityEditInfo(boolean visibility) {
        editDateLabel.setVisible(visibility);
        editDatePicker.setVisible(visibility);
        editTimePicker.setVisible(visibility);
        saveOperationButton.setVisible(visibility);
        if (visibility) {
            toggleEditInfoButton.setText(Messages.EDIT_INFO_HIDE_BUTTON.get());
            switchOperationButton.setVisible(false);
            importFileButton.setVisible(false);
        } else {
            toggleEditInfoButton.setText(Messages.EDIT_INFO_SHOW_BUTTON.get());
            switchOperationButton.setVisible(true);
            importFileButton.setVisible(true);
        }
    }

    /**
     * Set the visibility of the edit info toggle button.
     *
     * @param visibility the new visibility.
     */
    private void setVisibilityToggleEditInfo(boolean visibility) {
        importFileButton.setVisible(visibility);
        toggleEditInfoButton.setVisible(visibility);
        switchOperationButton.setVisible(visibility);
    }

    /**
     * Setup the listener for the new operation button.
     */
    private void newOperationButtonListener() {
        newOperationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibilityOperationButtons(false);
                setVisibilityNewOperation(true);
            }
        });
    }

    /**
     * Setup the listener for the existing operation button.
     */
    private void existingOperationButtonListener() {
        existingOperationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibilityOperationButtons(false);
                setVisibilityExistingOperation(true);
            }
        });
    }

    /**
     * Setup the listener for the edit info toggle button.
     */
    private void toggleEditInfoButtonListener() {
        toggleEditInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (toggleEditInfoButton.getText().equals(Messages.EDIT_INFO_SHOW_BUTTON.get())) {
                    setVisibilityEditInfo(true);
                    toggleEditInfoButton.setText(Messages.EDIT_INFO_HIDE_BUTTON.get());
                } else if (toggleEditInfoButton.getText().equals(Messages.EDIT_INFO_HIDE_BUTTON.get())) {
                    setVisibilityEditInfo(false);
                    toggleEditInfoButton.setText(Messages.EDIT_INFO_SHOW_BUTTON.get());
                }
            }
        });
    }

    /**
     * Setup the listener for the button to register a new operation.
     */
    private void registerNewOperationButtonListener() {
        registerNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int day = datePicker.getDate().getDayOfMonth();
                int month = datePicker.getDate().getMonthValue();
                int year = datePicker.getDate().getYear();
                int hour = timePicker.getTime().getHour();
                int minute = timePicker.getTime().getMinute();
                String operationName = operationNameInput.getText();

                if (StringTools.isValidOperationName(operationName)) {
                    if (OPERATION_MANAGER.operationNameAlreadyExists(operationName)) {
                        errorMessageLabel.setText(Messages.OPERATION_NAME_ALREADY_EXISTS.get());
                        errorMessageLabel.setVisible(true);
                    } else {
                        Operation operation = new Operation(operationName, day, month, year, hour, minute);
                        OPERATION_MANAGER.setupOperation(operation);
                        setVisibilityNewOperation(false);
                        setVisibilityToggleEditInfo(true);
                        errorMessageLabel.setVisible(false);
                        awaitingGPSLabel.setVisible(true);
                    }
                } else {
                    errorMessageLabel.setText(Messages.INVALID_OPERATION_NAME.get());
                    errorMessageLabel.setVisible(true);
                }
            }
        });
    }

    /**
     * Setup the listener for the button to load an existing operation.
     */
    private void registerExistingOperationButtonListener() {
        registerExistingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOperationName = (String) existingOperationInput.getSelectedItem();
                for (Operation operation : OPERATION_MANAGER.getExistingOperations()) {
                    if (operation.getName().endsWith(selectedOperationName)) {
                        OPERATION_MANAGER.setupOperation(operation);
                        awaitingGPSLabel.setVisible(true);
                    }
                }
                setVisibilityExistingOperation(false);
                setVisibilityToggleEditInfo(true);
            }
        });
    }

    /**
     * Setup the listener for the button to save an edited operation.
     */
    private void saveOperationButtonListener() {
        saveOperationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int year = editDatePicker.getDate().getYear();
                int month = editDatePicker.getDate().getMonthValue();
                int day = editDatePicker.getDate().getDayOfMonth();
                int hour = editTimePicker.getTime().getHour();
                int minute = editTimePicker.getTime().getMinute();
                OPERATION_MANAGER.updateCurrentOperation(year, month, day, hour, minute);
                setVisibilityEditInfo(false);
            }
        });
    }

    /**
     * Setup the listener for the import file button
     */
    private void importFileButtonListener() {
        importFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
    }

    /**
     * Setup the listener for the button to switch operation
     */
    private void switchOperationListener () {
        switchOperationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibilityToggleEditInfo(false);
                setVisibilityOperationButtons(true);
            }
        });
    }

    /**
     * Setup the listener for the button to go back in both new operation and existing operation
     */
    private void backButtonListener() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibilityExistingOperation(false);
                setVisibilityNewOperation(false);
                setVisibilityOperationButtons(true);
            }
        });
    }
}
