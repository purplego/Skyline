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
	 * bnlSkyline���������ļ�inFilename�ж�ȡԭʼ���ݼ�������BNL�㷨���skyline��
	 * @param inFilename�����ԭʼ���ݼ����ļ�
	 * @return ���ش�žֲ�skyline��LinkedList��spList
	 */
	public static LinkedList<SkyTuple> bnlQuery(String inFilename){
		
		String filePathIn = Constants.FILEDIRECTORY + inFilename;
		File fileIn=new File(filePathIn);
		
		//����һ���ڵ�Ϊ�������͵��������ڴ�ź�ѡ��Skyline�㼰���ս��
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
						//����֧���ϵ���Ժ�����������������֮���֧���ϵ
						int isDominate = IsDominate.dominateBetweenTuples(spList.get(i), tuple);
						switch(isDominate){
						case 0:					//tuple��֧�䣬ֱ��ɾȥ�ö���
							flag = false;		//����whileѭ��������һ������
							break;
						case 1:					//tuple֧��spList�еĵ�i������ɾȥ�ö��󣬼�������Ƚϣ�������һ��ѭ��
							spList.remove(i);
							break;
						case 2:					//tuple��spList�еĵ�i�����󻥲�֧�䣬��������Ƚϣ�������һ��ѭ��
							i++;				//�Ƚ϶����Ϊ�����е���һ������
							break;
						default:
							break;
						}
						if(i==spList.size()){	//����Ƚϵ�spList�����һ�������tuple��Ȼδ��֧�䣬������������ı�β
							spList.addLast(tuple);
							flag = false;		//����whileѭ�������ļ��е���һ��
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
		//����һ���ڵ�Ϊ�������͵��������ڴ�ź�ѡ��Skyline�㼰���ս��
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
					//����֧���ϵ���Ժ�����������������֮���֧���ϵ
					int isDominate = IsDominate.dominateBetweenTuples(globalspList.get(i), tuple);
					switch(isDominate){
					case 0:					//tuple��֧�䣬ֱ��ɾȥ�ö���
						flag = false;		//����whileѭ��������һ������
//						countNotAdd++;
						break;
					case 1:					//tuple֧��spList�еĵ�i������ɾȥ�ö��󣬼�������Ƚϣ�������һ��ѭ��
						globalspList.remove(i);
//						countRemove ++;
						break;
					case 2:					//tuple��spList�еĵ�i�����󻥲�֧�䣬��������Ƚϣ�������һ��ѭ��
						i++;				//�Ƚ϶����Ϊ�����е���һ������
						break;
					default:
						break;
					}
					if(i==globalspList.size()){	//����Ƚϵ�spList�����һ�������tuple��Ȼδ��֧�䣬������������ı�β
						globalspList.addLast(tuple);
						flag = false;		//����whileѭ�������ļ��е���һ��
					}
				}
			}
		}
//		System.out.println("the number of tuples removed from the tuple_List in BNL is :" + countRemove);
//		System.out.println("the number of tuples not add into the tuple_List in BNL is :" + countNotAdd);
		return globalspList;
	}
}
