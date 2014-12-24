package sky.queryalgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import sky.model.SkyTuple;
import sky.util.Constants;
import sky.util.Hpreprocess;
/**
 * @author Purple Wang
 * Jan 20, 2014
 */
public class BNL {
	

	
	/**
	 * bnlSkyline方法，从文件inFilename中读取原始数据集，利用BNL算法求得skyline点
	 * @param inFilename，存放原始数据集的文件
	 * @return 返回存放局部skyline的LinkedList即spList
	 */
	public static LinkedList<SkyTuple> bnlQuery(String inFilename){
		
		String filePathIn = Constants.FILEDIRECTORY + inFilename;
		File fileIn=new File(filePathIn);
		
		//定义一个节点为数组类型的链表，用于存放候选的Skyline点及最终结果
		LinkedList<SkyTuple> spList = new LinkedList<SkyTuple>();
		Hpreprocess h_process = new Hpreprocess();
		try{
			FileReader fReader=new FileReader(fileIn);
			BufferedReader bReader=new BufferedReader(fReader);
			
			while(bReader.ready()){
				String str_line = bReader.readLine();
				SkyTuple tuple = h_process.buildTupleFromStr(str_line);
				if(spList.isEmpty())
					spList.add(tuple);
				else{
					int i = 0;
					boolean flag = true;
					while(flag){
						//调用支配关系测试函数，测试两个对象之间的支配关系
						int isDominate = IsDominate.dominateBetweenTuples(spList.get(i), tuple);
						switch(isDominate){
						case 0:					//tuple被支配，直接删去该对象
							flag = false;		//跳出while循环，读下一个对象
							break;
						case 1:					//tuple支配spList中的第i个对象，删去该对象，继续往后比较，进入下一次循环
							spList.remove(i);
							break;
						case 2:					//tuple与spList中的第i个对象互不支配，继续往后比较，进入下一次循环
							i++;				//比较对象变为链表中的下一个对象
							break;
						default:
							break;
						}
						if(i==spList.size()){	//如果比较到spList的最后一个对象而tuple仍然未被支配，则将其加入该链表的表尾
							spList.addLast(tuple);
							flag = false;		//结束while循环，读文件中的下一行
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return spList;
	}
	
	public static LinkedList<SkyTuple> bnlQuery(LinkedList<SkyTuple> localspsList){
		//定义一个节点为数组类型的链表，用于存放候选的Skyline点及最终结果
		LinkedList<SkyTuple> globalspList = new LinkedList<SkyTuple>();
//		int countRemove = 0;
//		int countNotAdd = 0;
		for(SkyTuple tuple: localspsList){
			if(globalspList.isEmpty())
				globalspList.add(tuple);
			else{
				int i = 0;
				boolean flag = true;
				while(flag){
					//调用支配关系测试函数，测试两个对象之间的支配关系
					int isDominate = IsDominate.dominateBetweenTuples(globalspList.get(i), tuple);
					switch(isDominate){
					case 0:					//tuple被支配，直接删去该对象
						flag = false;		//跳出while循环，读下一个对象
//						countNotAdd++;
						break;
					case 1:					//tuple支配spList中的第i个对象，删去该对象，继续往后比较，进入下一次循环
						globalspList.remove(i);
//						countRemove ++;
						break;
					case 2:					//tuple与spList中的第i个对象互不支配，继续往后比较，进入下一次循环
						i++;				//比较对象变为链表中的下一个对象
						break;
					default:
						break;
					}
					if(i==globalspList.size()){	//如果比较到spList的最后一个对象而tuple仍然未被支配，则将其加入该链表的表尾
						globalspList.addLast(tuple);
						flag = false;		//结束while循环，读文件中的下一行
					}
				}
			}
		}
//		System.out.println("the number of tuples removed from the tuple_List in BNL is :" + countRemove);
//		System.out.println("the number of tuples not add into the tuple_List in BNL is :" + countNotAdd);
		return globalspList;
	}
}
