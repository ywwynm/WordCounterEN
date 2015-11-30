package ui;

import counter.Counter;
import decoder.StringDecoderFactory;
import utils.Algorithms;
import utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 张启 on 2015/11/24.
 * Main user interface.
 */
public class MainForm {

    private File mFileToCount;
    private File mSavedFile;

    private JPanel mainPanel;

    private JFileChooser mChooser;

    @SuppressWarnings("unused")
    private JLabel filePathLabel;

    private JTextField pathTextField;

    private JCheckBox ignoreCaseCheckBox;

    private JButton pathButton;
    private JButton countCharactersButton;
    private JButton countWordsButton;

    public MainForm() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mChooser = new JFileChooser();
        mChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        setListeners();
    }

    private void setListeners() {
        pathButton.addActionListener(e -> chooseFile());

        countCharactersButton.addActionListener(
                e -> doCountingWorks(Counter.COUNTER_MODE_CHARACTER));

        countWordsButton.addActionListener(
                e -> doCountingWorks(Counter.COUNTER_MODE_WORD));
    }

    private void chooseFile() {
        mChooser.showOpenDialog(mainPanel);
        mFileToCount = mChooser.getSelectedFile();
        if (mFileToCount != null) {
            pathTextField.setText(mFileToCount.getAbsolutePath());
        }
    }

    private void doCountingWorks(int mode) {
        if (!FileUtils.isFileValid(mFileToCount)) {
            alertFailureAtOpeningFile();
            return;
        }

        try {
            String content = StringDecoderFactory.decodeFromFile(mFileToCount);
            Counter counter = new Counter(content);
            HashMap map = counter.count(mode, ignoreCaseCheckBox.isSelected());

            /*
                The very only possibility of map == null is that parameter mode isn't
                0 or 1, which cannot appear because it is decided by clicking existed
                two buttons.
             */
            assert map != null;

            boolean created = createFileToSave(mode);
            if (!created) {
                JOptionPane.showMessageDialog(null, "Cannot create result file.");
                return;
            }

            List<Map.Entry> entries = Algorithms.sortMapByValue(map, true);
            saveResultToFile(entries);
            showDialogAfterCounting();
        } catch (Exception e) {
            // cannot open the file
            alertFailureAtOpeningFile();
            e.printStackTrace();
        }
    }

    private void saveResultToFile(List<Map.Entry> entries) {
        BufferedWriter bufferedWriter = null;
        try {
            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(mSavedFile));
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("Length of original text is "
                    + mFileToCount.length() + ".\r\n\r\n");
            for (Map.Entry entry : entries) {
                bufferedWriter.write(entry.getKey() + ":" + entry.getValue() + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    // Don't know if this can also close writer since I don't
                    // find writer.close() in BufferedWriter.java.
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean createFileToSave(int mode) {
        File parent = mFileToCount.getParentFile();
        if (!FileUtils.isFileValid(parent)) {
            return false;
        }

        String name = generateCountFileName(mode);

        // Cannot be null unless mode isn't 0 or 1.
        assert name != null;

        mSavedFile = new File(parent, name);
        return true;
    }

    private String generateCountFileName(int mode) {
        String postfix;
        if (mode == Counter.COUNTER_MODE_CHARACTER) {
            postfix = "_character_count.txt";
        } else if (mode == Counter.COUNTER_MODE_WORD) {
            postfix = "_word_count.txt";
        } else {
            return null;
        }
        return FileUtils.getFileName(mFileToCount) + postfix;
    }

    private void showDialogAfterCounting() throws IOException {
        int clicked = JOptionPane.showConfirmDialog(null,
                "Do you want to check the result now?",
                "Counting Finished!", JOptionPane.YES_NO_OPTION);
        if (clicked == 0) {
            Desktop.getDesktop().open(mSavedFile);
        }
    }

    private void alertFailureAtOpeningFile() {
        JOptionPane.showMessageDialog(null,
                "Cannot open the file. Please check if it exists " +
                "or you are permitted to open it.");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Select file");

        frame.setSize(480, 150);

        MainForm form = new MainForm();
        frame.setContentPane(form.mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
