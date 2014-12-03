/* 2014/11/20 作成
 * アセンブリ用音声ファイルを作成するプログラム
 */
package lego;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;

import java.awt.ScrollPane;
import java.awt.Label;

import javax.swing.JPopupMenu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JRadioButtonMenuItem;

import java.awt.Choice;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JScrollBar;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.Action;

import java.util.*;
import java.awt.Color;

public class Music extends JFrame {
    public ArrayList<Scale> scale = new ArrayList<Scale>(); //音階と周波数を保存

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Music frame = new Music();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private int rowNum = 0; //JTableに存在する行の総数
    Sound s = null;

    private String[][] tableData = {{"1", "ラ(440)", "0"}}; //最初の行
    private String[] columnNames = {"", "音", "秒数"}; //一番上
    private JTable table;
    private DefaultTableModel tableModel;
    private final Action add = new SwingAction_add();
    private final Action remove = new SwingAction_remove();
    private final Action play = new SwingAction_play();
    private final Action export = new SwingAction_export();
    private final Action save = new SwingAction_save();
    private final Action open = new SwingAction_open();
    private final Action stop = new SwingAction_stop();
    /**
     * Create the frame.
     */
    public Music() {
        scale.add(new Scale("無音", 0, 0));
        /*
        scale.add(new Scale("ラ(110)", 110));
        scale.add(new Scale("ラ#(117)", 117));
        scale.add(new Scale("シ(123)", 123));
        scale.add(new Scale("ド(131)", 131));
        scale.add(new Scale("ド#(139)", 139));
        scale.add(new Scale("レ(147)", 147));
        scale.add(new Scale("レ#(156)", 156));
        scale.add(new Scale("ミ(165)", 165));
        scale.add(new Scale("ファ(174)", 174));
        scale.add(new Scale("ファ#(185)", 185));
        scale.add(new Scale("ソ(196)", 196));
        scale.add(new Scale("ソ#(208)", 208));
        */
        scale.add(new Scale("ラ(220)", 220, 70));
        scale.add(new Scale("ラ#(233)", 233, 66));
        scale.add(new Scale("シ(247)", 247, 62));
        scale.add(new Scale("ド(262)", 262, 60));
        scale.add(new Scale("ド#(277)", 277, 56));
        scale.add(new Scale("レ(294)", 294, 52));
        scale.add(new Scale("レ#(311)", 311, 50));
        scale.add(new Scale("ミ(330)", 330, 46));
        scale.add(new Scale("ファ(349)", 349, 44));
        scale.add(new Scale("ファ#(370)", 370, 42));
        scale.add(new Scale("ソ(392)", 392, 40));
        scale.add(new Scale("ソ#(415)", 415, 38));
        scale.add(new Scale("ラ(440)", 440, 36));
        scale.add(new Scale("ラ#(466)", 466, 34));
        scale.add(new Scale("シ(494)", 494, 32));
        scale.add(new Scale("ド(523)", 523, 30));
        scale.add(new Scale("ド#(554)", 554, 28));
        scale.add(new Scale("レ(587)", 587, 26));
        scale.add(new Scale("レ#(622)", 622, 24));
        scale.add(new Scale("ミ(659)", 659, 22));
        scale.add(new Scale("ファ(698)", 698, 21));
        scale.add(new Scale("ファ#(740)", 740, 20));
        scale.add(new Scale("ソ(784)", 784, 19));
        scale.add(new Scale("ソ#(831)", 831, 18));
        scale.add(new Scale("ラ(880)", 880, 17));
        scale.add(new Scale("ラ#(932)", 932, 16));
        scale.add(new Scale("シ(988)", 988, 15));
        scale.add(new Scale("ド(1047)", 1047, 14));
		scale.add(new Scale("ド#(1109)", 1109, 13));
		scale.add(new Scale("レ(1175)", 1175, 12));
		scale.add(new Scale("レ#(1245)", 1245, 11));
		/*
		scale.add(new Scale("ミ(1319)", 1319, 10));
		scale.add(new Scale("ファ(1397)", 1397, 9));
		scale.add(new Scale("ファ#(1480)", 1480, 8));
		*/
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 360, 591);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.window);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //table
        tableModel = new DefaultTableModel(tableData, columnNames);
        table = new JTable(tableModel);
        table.setBackground(SystemColor.textHighlight);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.changeSelection(0, 0, false, false);
        TableColumn col = table.getColumnModel().getColumn(0);
        col.setMinWidth(30);
        col.setMaxWidth(30);

        JComboBox comboBox = new JComboBox();
        for (int i = 0; i < scale.size(); i++) {
        	comboBox.addItem(scale.get(i).scaleName);
        }

        TableCellEditor editor = new DefaultCellEditor(comboBox);
        table.getColumnModel().getColumn(1).setCellEditor(editor); //2列目


        JScrollPane scroll = new JScrollPane(table);
        scroll.setSize(300, 400);
        scroll.setLocation(16, 55);

        contentPane.add(scroll);

        //Playボタン
        JButton btn_play = new JButton("Play");
        btn_play.setAction(play);
        btn_play.setBounds(6, 6, 117, 29);
        contentPane.add(btn_play);

        //stopボタン
        JButton btn_stop = new JButton("Stop");
        btn_stop.setAction(stop);
        btn_stop.setBounds(137, 6, 117, 29);
        contentPane.add(btn_stop);

        //addボタン
        JButton btn_add = new JButton("追加");
        btn_add.setAction(add);
        btn_add.setBounds(15, 460, 117, 29);
        contentPane.add(btn_add);

        //removeボタン
        JButton btn_remove = new JButton("削除");
        btn_remove.setAction(remove);
        btn_remove.setBounds(144, 460, 117, 29);
        contentPane.add(btn_remove);

        //exportボタン
        JButton btn_export = new JButton("Export");
        btn_export.setAction(export);
        btn_export.setBounds(16, 520, 117, 29);
        contentPane.add(btn_export);

        //saveボタン
        JButton btn_save = new JButton("Save");
        btn_save.setAction(save);
        btn_save.setBounds(162, 520, 92, 29);
        contentPane.add(btn_save);

        //openボタン
        JButton btn_open = new JButton("Open");
        btn_open.setAction(open);
        btn_open.setBounds(255, 520, 85, 29);
        contentPane.add(btn_open);
    }

    //ボタンを押した時の動作
    private class SwingAction_add extends AbstractAction {
        public SwingAction_add() {
            putValue(NAME, "Add");
            putValue(SHORT_DESCRIPTION, "Some short description");
        }
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow(); //選択行を取得
            rowNum++;
            Object[] temp = {row+2, "", "0"};
            tableModel.insertRow(row+1, temp); //行をrow+1に挿入
            for (int i = row+2; i <= rowNum; i++) {
                tableModel.setValueAt(i+1, i, 0); //1列目を変更
            }
        }
    }

    private class SwingAction_remove extends AbstractAction {
        public SwingAction_remove() {
            putValue(NAME, "Remove");
            putValue(SHORT_DESCRIPTION, "Some short description");
        }
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            tableModel.removeRow(row);
            rowNum--;
            for (int i = row; i <= rowNum; i++) {
                tableModel.setValueAt(i+1, i, 0);
            }
            if (row > rowNum)
            	row--;
            table.changeSelection(row, 0, false, false); //自動で選択するように
        }
    }

    private class SwingAction_export extends AbstractAction {
		public SwingAction_export() {
			putValue(NAME, "Export");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			double[][] sound = storage();
			try {
				File file = new File("Music");
				FileWriter filewriter = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(filewriter);
				PrintWriter pw = new PrintWriter(bw);

				pw.println("\t .section .data");

				pw.print("sound: .word ");
				for (int i = 0; i < sound.length; i++) {
				/*
					double tmp = 15625/sound[i][0]; // 16000000/1024/周波数
					int output = (int) tmp;
					if (tmp > 255) {
						output = 0;
					}
					if ((int) (tmp % 2) != 0 && tmp % 2 < 1.5) {
						output = (int) (tmp-1);
					} else if ((int) (tmp % 2) != 0 && tmp % 2 >= 1.5) {
						output = (int) (tmp+1);
					}
					if (sound[i][0] == 0) { //無音の場合
						output = 0;
					}
				*/
					int output = (int) (sound[i][2]);
					pw.print(output);
					pw.print(", ");

					output = (int) ( (sound[i][1]*1000) - 1);  //(長さ*1000)-1
					if (output < 0) {
						output = 0;
					}
					pw.print(output);
					if (i != sound.length-1) {
						pw.print(", ");
					} else {
						pw.println();
					}
				}

				/*
				pw.print("length: .word ");
				for (int i = 0; i < sound.length; i++) {
					int tmp = (int) ( (sound[i][1]*1000) - 1);
					if (tmp < 0) {
						tmp = 0;
					}
					pw.print(tmp);
					if (i != sound.length-1) {
						pw.print(", ");
					} else {
						pw.println();
					}
				}
				*/

				pw.println("soundData: .word " + sound.length);

				pw.close();
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		}
	}

    private class SwingAction_play extends AbstractAction {
        public SwingAction_play() {
            putValue(NAME, "Play");
            putValue(SHORT_DESCRIPTION, "Some short description");
        }
        public void actionPerformed(ActionEvent e) {
            double[][] sound = storage();
            s = new Sound(sound);
            s.play(); //wav再生
        }
    }

    private class SwingAction_stop extends AbstractAction {
		public SwingAction_stop() {
			putValue(NAME, "Stop");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			if (s != null)
				s.stop(); //再生停止
		}
	}

    private class SwingAction_save extends AbstractAction {
		public SwingAction_save() {
			putValue(NAME, "Save");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();

			JFrame frame = new JFrame();
			frame.setBounds(100, 100, 450, 300);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			int selected = fileChooser.showSaveDialog(frame); //保存用の画面を出す
			if (selected == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				saveFile(selectedFile);
			}
		}
	}

    private class SwingAction_open extends AbstractAction {
		public SwingAction_open() {
			putValue(NAME, "Open");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();

			JFrame frame = new JFrame();
			frame.setBounds(100, 100, 450, 300);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			int selected = fileChooser.showOpenDialog(frame); //選択用の画面を出す
			if (selected == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				openFile(selectedFile);
			}
		}
	}

    private double[][] storage() {
    	double[][] sound = new double[rowNum+1][3];
        for (int i = 0; i <= rowNum; i++) {
            for (int j = 0; j < scale.size(); j++) {
                if (scale.get(j).scaleName.equals(table.getValueAt(i, 1).toString())) { //音階と周波数が一致した場合
                    sound[i][0] = scale.get(j).freq;
                    sound[i][1] = new Double(table.getValueAt(i, 2).toString());
                    sound[i][2] = scale.get(j).freq_asm;
                    break;
                } else if (table.getValueAt(i, 1).toString().equals("") ||
             					table.getValueAt(i, 2).toString().equals("")) { //何も入力がない場合
                  	JFrame warning = new JFrame("確認して下さい");
                   	warning.setSize(300, 100);
                   	warning.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                   	warning.setLocation(contentPane.getLocation());
                   	Container contentPane = warning.getContentPane();
                   	JLabel label = new JLabel("指定していない値が存在します" + "(" + (i+1) + ")");
                   	label.setBounds(40, 40, warning.getLocation().x/2, warning.getLocation().y/2);
                   	contentPane.add(label);
                   	warning.setVisible(true);
                   	return sound;
                }
            }
        }
    	return sound;
    }

    private void saveFile(File file) {
		try {
			FileWriter filewriter = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(filewriter);
			PrintWriter pw = new PrintWriter(bw);

			double[][] sound = storage();

			for (int i = 0; i < sound.length; i++) {
				pw.println(sound[i][0]);
				pw.println(sound[i][1]);
			}
			pw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }

    private void openFile(File file) {
    	try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);

			ArrayList<Double> freq = new ArrayList<Double>(); //周波数保存
			ArrayList<Double> length = new ArrayList<Double>(); //長さ保存

			String str = br.readLine();
			while (str != null) {
				freq.add(new Double(str));

				str = br.readLine(); //ファイルから1行読み取る
				length.add(new Double(str));

				str = br.readLine();
			}

			tableModel.setRowCount(0); //現在存在する全ての行を削除

			for (int i = 0; i < freq.size(); i++) {
				String scaleName = new String();
				for (int j = 0; j < scale.size(); j++) {
					if (freq.get(i) == scale.get(j).freq) { //音階と周波数が一致した場合
						scaleName = scale.get(j).scaleName;
						break;
					}
				}
				Object[] row = {(i+1), new String(scaleName), length.get(i)};
				tableModel.insertRow(i, row); //行を挿入
			}
			rowNum = freq.size()-1;
			br.close();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }
}
