package com.tonsincs.util;

import javax.sound.sampled.*;

import com.tonsincs.main.JQ_Main;

import java.io.*;

/**
* @ProjectName:JQueue
* @ClassName: SayNumbers
* @Description: TODO(实现语音播报核心组件)
* @author 萧达光
* @date 2014-5-28 下午01:30:53
* 
* @version V1.0 
*/
public class SayNumbers implements Runnable {
	private String number;
	private int counter;
	public static boolean sayingTheNumber = false;

	public void say(String number, int counter) {
		if (sayingTheNumber == false) {
			this.number = number;
			this.counter = counter;
			new Thread(this).start();
		}
	}

	public void SayWord(String key) {
		// if (wordID < 0)
		// wordID = 0;
		// if (wordID > 56)
		// wordID = 56;
		// String[] theWord = { "D://yuyin//YUEYU//0.wav", // 0
		// "D://yuyin//YUEYU//1.wav", // 1
		// "D://yuyin//YUEYU//2.wav", // 2
		// "D://yuyin//YUEYU//3.wav", // 3
		// "D://yuyin//YUEYU//4.wav", // 4
		// "D://yuyin//YUEYU//5.wav", // 5
		// "D://yuyin//YUEYU//6.wav", // 6
		// "D://yuyin//YUEYU//7.wav", // 7
		// "D://yuyin//YUEYU//8.wav", // 8
		// "D://yuyin//YUEYU//9.wav", // 9
		// "D://yuyin//YUEYU//A.wav", // 10
		// "D://yuyin//YUEYU//B.wav", // 11
		// "D://yuyin//YUEYU//C.wav", // 12
		// "D://yuyin//YUEYU//D.wav", // 13
		// "D://yuyin//YUEYU//E.wav", // 14
		// "D://yuyin//YUEYU//F.wav", // 15
		// "D://yuyin//YUEYU//G.wav", // 15
		// "D://yuyin//YUEYU//H.wav", // 17
		// "D://yuyin//YUEYU//I.wav", // 18
		// "D://yuyin//YUEYU//J.wav", // 19
		// "D://yuyin//YUEYU//K.wav", // 20
		// "D://yuyin//YUEYU//L.wav", // 21
		// "D://yuyin//YUEYU//M.wav", // 22
		// "D://yuyin//YUEYU//N.wav", // 23
		// "D://yuyin//YUEYU//O.wav", // 24
		// "D://yuyin//YUEYU//P.wav", // 25
		// "D://yuyin//YUEYU//Q.wav", // 26
		// "D://yuyin//YUEYU//R.wav", // 27
		// "D://yuyin//YUEYU//S.wav", // 28
		// "D://yuyin//YUEYU//T.wav", // 29
		// "D://yuyin//YUEYU//U.wav", // 30
		// "D://yuyin//YUEYU//V.wav", // 31
		// "D://yuyin//YUEYU//W.wav", // 32
		// "D://yuyin//YUEYU//X.wav", // 33
		// "D://yuyin//YUEYU//Y.wav", // 34
		// "D://yuyin//YUEYU//Z.wav", // 35
		// "D://yuyin//YUEYU//bell.wav", // 36
		// "D://yuyin//YUEYU//Counter1.wav", // 37
		// "D://yuyin//YUEYU//Counter2.wav", // 38
		// "D://yuyin//YUEYU//Counter3.wav", // 39
		// "D://yuyin//YUEYU//Counter4.wav", // 40
		// "D://yuyin//YUEYU//Counter5.wav", // 41
		// "D://yuyin//YUEYU//Counter6.wav", // 42
		// "D://yuyin//YUEYU//Counter7.wav", // 43
		// "D://yuyin//YUEYU//Counter8.wav", // 44
		// "D://yuyin//YUEYU//Counter9.wav", // 45
		// "D://yuyin//YUEYU//Counter10.wav", // 46
		// "D://yuyin//YUEYU//Counter11.wav", // 47
		// "D://yuyin//YUEYU//Counter12.wav", // 48
		// "D://yuyin//YUEYU//Counter13.wav", // 49
		// "D://yuyin//YUEYU//Counter14.wav", // 50
		// "D://yuyin//YUEYU//Counter15.wav", // 51
		// "D://yuyin//YUEYU//Counter16.wav", // 52
		// "D://yuyin//YUEYU//Counter17.wav", // 53
		// "D://yuyin//YUEYU//help.wav", // 54
		// "D://yuyin//YUEYU//Yplease.wav", // 请55
		// "D://yuyin//YUEYU//Yto.wav", // 报到56
		// };
		try {
			File f = new File(JQ_Main.OS_CONTEXT.get(key));
			// AudioInputStream source = AudioSystem
			// .getAudioInputStream(getClass().getResource(
			// theWord[wordID] + ".wav"));
			AudioInputStream source = AudioSystem
					.getAudioInputStream(f.toURL());
			Clip newClip = (Clip) AudioSystem.getLine(new DataLine.Info(
					Clip.class, source.getFormat()));
			newClip.open(source);
			newClip.start();
			try {
				Thread.sleep(11);
			} catch (InterruptedException e) {
			}
			while (newClip.isRunning()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
			newClip.close();
			newClip=null;
		} catch (IOException e) {
		} catch (LineUnavailableException e) {
		} catch (UnsupportedAudioFileException e) {
		}
	}

	public void run() {
		sayingTheNumber = true;
		//System.out.println(number);
		SayWord("bell");
		SayWord("Yplease");
		char[] numb = number.toCharArray();

		for (int i = 0; i < numb.length; i++) {
			//System.out.println(numb[i]);
			SayWord(numb[i] + "");
		}

		SayWord("Yto");
		SayWord("Counter" + counter);
		sayingTheNumber = false;
	}

	public static void main(String[] args) throws InterruptedException {
		// for (int i = 0; i < args.length; i++) {
		// //new Thread(new SayNumbers());
		// SayNumbers say = new SayNumbers();
		// say.say("A001", 2);
		// }
		SayNumbers say = new SayNumbers();
		say.say("A001", 2);
		//say.say("A002", 2);
	}
}