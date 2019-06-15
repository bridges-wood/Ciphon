import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class Tester {

	Tester() {
		run();
	}
	
	public void run() {
		String text = "YOUR MAJESTY,\\r\\n\" + \r\n"
				+ "				\"IT HAS BEEN MY DEEPEST PLEASURE AND A GREAT PRIVILEGE TO SERVE AS YOU PRIVATE SECRETARY THESE LAST NINE YEARS, AND THOUGH IT WOULD BE MY ARDENT WISH TO CONTINUE TO SERVE YOU THROUGHOUT YOUR REIGN, WE ARE, NONE OF US, IMMORTAL, AND MY THOUGHTS HAVE TURNED TO MY SUCCESSOR.\\r\\n\" + \r\n"
				+ "				\"YOU ARE OF COURSE ENTITLED TO DISREGARD MY ADVICE, HOWEVER I HAVE GIVEN CONSIDERATION TO THE CHANGES IN MY ROLE OVER THE LAST SEVERAL YEARS. AS YOUR EMPIRE HAS GROWN IN MAGNIFICENCE, OTHERS ACROSS OUR CONTINENT HAVE GROWN FRACTIOUS, AND AN INCREASING PORTION OF MY TIME IS SPENT MANAGING THE IMPACT OF THEIR QUARRELS UPON OUR ISLAND. I HAVE FELT, AT TIMES, LIKE STEPHENSON�S DR JEKYLL AS I HAVE MANAGED YOUR HOUSEHOLD AFFAIRS AND THE MORE PUBLIC ASPECTS OF YOUR STATE. AT OTHERS I HAVE BEEN PRESSED TO UNDERTAKE THE ROLE OF MR HYDE, FOCUSSING MY RAGE UNDER GREAT PROVOCATION FROM THE POWERS THAT THREATEN TO ASSAIL US. WITH THESE REFLECTIONS I HAVE COME TO THE VIEW THAT THE TIME MAY HAVE COME TO DISSECT THE ROLE OF PRIVATE SECRETARY INTO ITS TWO VERY SEPARATE FUNCTIONS.\\r\\n\" + \r\n"
				+ "				\"THE PUBLIC FACE OF THE ROYAL HOUSEHOLD MUST OF COURSE CONTINUE TO BE PRESENTED BY SOMEONE OF GRACE AND DIGNITY WHO CAN COMMAND THE CONFIDENCE OF THE COURTIERS. WE HAVE DISCUSSED AT LENGTH WHO MIGHT FILL THAT ROLE WHEN I EVENTUALLY PASS ON, AND I BELIEVE THAT WE HAVE AGREED TO INVITE PONSONBY TO INHERIT THAT MANTLE. HE IS A GOOD MAN AND WILL SERVE YOU WELL. I WOULD SUGGEST THAT FOR THE SAKE OF CONTINUITY HE CONTINUES TO CARRY THE TITLE OF PRIVATE SECRETARY AND WILL BE HAPPY TO PREPARE HIM FOR THIS ROLE. HOWEVER THERE ARE SOME ASPECTS OF MY ACTIVITIES THAT I SUSPECT THAT PONSONBY WOULD STRUGGLE TO ACCOMPLISH AND FOR THOSE I WOULD INVITE YOU TO CONSIDER A NEW POSITION IN YOUR HOUSEHOLD, THAT OF SECRET SECRETARY.\\r\\n\" + \r\n"
				+ "				\"IN THESE QUARRELSOME TIMES IT MAY BE NECESSARY TO COMMISSION ACTIONS OR ENQUIRIES THAT SOME MIGHT REGARD AS BENEATH THE DIGNITY OF THE CROWN. THE SECRET SECRETARY CAN, BY CONCEALING THESE ACTIVITIES, PRESERVE THE REPUTATION OF YOUR GOVERNMENT AS A RELIABLE AND TRUSTWORTHY PARTICIPANT IN INTERNATIONAL AFFAIRS, WHILE ALSO PROVIDING YOU AND YOUR MINISTERS WITH THE WEAPONS TO DEFEAT OUR ENEMIES. IF WE SUCCEED AS I HOPE WE WILL, THEN WARS OF THE FUTURE MAY BE WON WITHOUT A SHOT BEING FIRED.\\r\\n\" + \r\n"
				+ "				\"IT IS MY FERVENT HOPE THAT YOU AGREE WITH MY ANALYSIS AND THAT TOGETHER WE CAN MOVE TO ESTABLISH THE NEW OFFICE. I HAVE SEVERAL NAMES THAT I WOULD HUMBLY SUGGEST AS STRONG CANDIDATES FOR THE NEW ROLE. ALL ARE GOOD MEN, WITH MILITARY BACKGROUNDS AND A REPUTATION FOR HONOUR THAT NO-ONE COULD QUESTION. I WILL BE HAPPY TO DISCUSS THIS FURTHER AT YOUR PLEASURE.\\r\\n\" + \r\n"
				+ "				\"YOUR FAITHFUL SERVANT,\\r\\n\" + \r\n" + "				\"CHARLES GREY";
		//String otherText = "KUSWRVZOULTHPTWGOVZOULFRJTKAS";
		//String otherOtherText = "King�sisaveryspecialplace,themomentyouwalkthroughthedoorsyoucansensethewarmth,purposeandenergyofallthepupilsandstaff.Thisisaschoolwhichvaluespositiverelationships,opportunityandgreatlearningaboveallelse,therebyallowingourpupilstomaketheverymostofthemselvesataschooltheylove.";
		// for (String word : PredictWords.predictedWords("complexity", true)) { // Test
		// single word prediction.
		// System.out.println(word);
		// }
		// for (String word : MultiLemmaAnalysis.possibleLemmata("this is a sentence"))
		// { // Test multi word prediction.
		// System.out.println(word);
		// }
		// for (Map.Entry<String, Integer> entry : NGramAnalyser.kasiskiBase(3,
		// otherText).entrySet()) {
		// System.out.println(entry.getKey() + ":" + entry.getValue());
		// }
		//int[] keys = KasiskiExamination.likelyKeyLengths(NGramAnalyser.kasiskiBase(3, otherText), otherText);
		//System.out.println(KasiskiExamination.keyGuesserVigenere(KasiskiExamination.mostLikelyKeyLength(otherText, keys), otherText));
		// System.out.println(DetectEnglish.detectEnglish(otherText));
		// Utilities.generateObjectHashTable("2grams.txt ", "2grams.ohtb");
		// System.out.println(Utilities.readHashTable("hashed_dictionary.htb").keySet().contains(Utilities.hash64("ghgashdfj")));
		// DetectEnglish a = new DetectEnglish();
		// System.out.println(a.respace(otherOtherText));
		try {
			System.out.println(InetAddress.getByName("192.168.1.166"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Tester a = new Tester();
	}

}
