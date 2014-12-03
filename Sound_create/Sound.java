/*
 * 2014/11/20 作成
 * 指定した値, 時間の正弦波を出力し, wavへの変換を行うプログラム
 */
package lego;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.sound.sampled.*;



public class Sound {
	private double[][] sound; //格納用
	private static final int SAMPLING = 44100; //サンプリング周波数
	private static final String fileName = "wave.txt";
	private static final String outputName = "sound.wav";
	Clip line = null; //wav再生用

	public Sound(double[][] sound) {
		this.sound = sound;
	}

	private void create() {
		try {
			File file = new File(fileName);
			FileWriter filewriter = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(filewriter);
			PrintWriter pw = new PrintWriter(bw);
			int file_rowNum = 0;

			for (int i = 0; i < sound.length; i++) {
				double time = sound[i][1]; //鳴らす時間
				int size = (int) (SAMPLING * time); //サイズ
				double freq = sound[i][0]; //周波数
				double t;

				for (int j = 0; j < size; j++) {
					t = (double) j / SAMPLING;
					int sound = (int) (127 * Math.sin(2.0 * Math.PI * freq * t) );
					pw.println(sound);
					file_rowNum++;
				}
			}
			pw.close();


			/*
			ProcessBuilder pb = new ProcessBuilder("./txt2wav", fileName, outputName);
			Process process = pb.start(); //txt2wavを実行
			*/

			byte[] byte_file = new byte[file_rowNum];
			int count = 0;
			FileReader reader = new FileReader(fileName);
			BufferedReader br = new BufferedReader(reader);

			String str = br.readLine();
			while(str != null) {
				byte_file[count] = new Byte(str);
				str = br.readLine();
				count++;
			}

			AudioFormat audioFormat = new AudioFormat(SAMPLING, 8, 1, true, false);
			AudioInputStream ais = new AudioInputStream(
					new ByteArrayInputStream(byte_file), audioFormat, byte_file.length);
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(outputName));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			System.out.println(e);
		}
	}

	public void play() {
		create();

		AudioFormat format = null;
		DataLine.Info info = null;
        line = null;
        File audioFile = null;

        try {
        	audioFile = new File(outputName);
        	format = AudioSystem.getAudioFileFormat(audioFile).getFormat();
        	info = new DataLine.Info(Clip.class, format);
        	line = (Clip) AudioSystem.getLine(info);
        	line.open(AudioSystem.getAudioInputStream(audioFile));
        	line.start(); //wav再生
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	public void stop() {
		line.stop(); //再生停止
	}
}
