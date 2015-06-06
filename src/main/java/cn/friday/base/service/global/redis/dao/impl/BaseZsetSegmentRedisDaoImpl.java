package cn.friday.base.service.global.redis.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import cn.friday.base.service.global.redis.bo.ZsetResult;
import cn.friday.base.service.global.redis.dao.IBaseZsetRedisDao;
import cn.friday.base.service.global.redis.dao.IRedisOpsTemplate;

public abstract class BaseZsetSegmentRedisDaoImpl implements IBaseZsetRedisDao,IRedisOpsTemplate {
	
	private String baseKey;
	/**
	 * 多少个分片
	 */
	private int SEGMENT_SIZE = 1;
	/**
	 * 每个分片对应的key
	 */
	private String [] key;
	/**
	 * 第一个分片元素大小
	 */
	private int SEGMENT_INITIAL_SIZE = 10000;
	
	public BaseZsetSegmentRedisDaoImpl(String baseKey, int segmentSize){
		this.SEGMENT_SIZE = segmentSize;
		this.baseKey = baseKey;
		buildSegmentBaseKey();
	}

	@Override
	public boolean add(String member, double score, int... ids) {
		//获取元素所处的分片
		int segmentIndex = getElenmentInSegment(member, ids);
		for(int i=0; i<SEGMENT_SIZE; i++){
			String segmentKey = buildKey(i, ids);
			//获取第一分片的大小
			long segmentSize = getSegmentElementSize(segmentKey);
			int maxSegmentSize = getSegmentSize(i);
			if(segmentSize < maxSegmentSize || maxSegmentSize == -1){
				
				//将元素添加到里面
				if(segmentIndex == -1){
					//加入到当前分片，将包含最后一个分片的数据都向后移动
					stringRedisTemplate().opsForZSet().add(segmentKey, member, score);
				}else if(segmentIndex == i){
					stringRedisTemplate().opsForZSet().add(segmentKey, member, score);
				}else if(segmentIndex < i){
					stringRedisTemplate().opsForZSet().add(segmentKey, member, score);
					//在大的分值处移走对应的member
					remove(member, ids);
				}else if(segmentIndex > i){
					//加入到当前分片，将包含最后一个分片的数据都向后移动
					stringRedisTemplate().opsForZSet().add(segmentKey, member, score);
					stringRedisTemplate().opsForZSet().remove(buildKey(segmentIndex, ids), member);
					afterMoveSegment(i, maxSegmentSize, segmentKey,ids);
				}
				
				break;
			}else{
					//获取最后一个元素（e）的分值e1，
					ZsetResult zsetResult = getSegmentLastElement(segmentKey, maxSegmentSize);
					if(zsetResult.getScore() < score){
						if(segmentIndex == -1){
							//加入到当前分片，将包含最后一个分片的数据都向后移动
							stringRedisTemplate().opsForZSet().add(segmentKey, member, score);
							afterMoveSegment(i, maxSegmentSize, segmentKey,ids);
						}else if(segmentIndex == i){
							stringRedisTemplate().opsForZSet().add(segmentKey, member, score);
						}else if(segmentIndex < i){
							stringRedisTemplate().opsForZSet().add(segmentKey, member, score);
							//在大的分值处移走对应的member
							remove(member, ids);
						}else if(segmentIndex > i){
							//加入到当前分片，将包含最后一个分片的数据都向后移动
							stringRedisTemplate().opsForZSet().add(segmentKey, member, score);
							stringRedisTemplate().opsForZSet().remove(buildKey(segmentIndex, ids), member);
							afterMoveSegment(i, maxSegmentSize, segmentKey,ids);
						}
						break;
					}

				
			}
		}
		return true;
	}
	/**
	 * 增加多个成员
	 * @param tuples
	 * @param ids
	 * @return
	 */
	public boolean add(Set<TypedTuple<String>> tuples, int ... ids){
		return false;
	}
	
	/**
	 * 获取member处在哪个分片
	 * @param member
	 * @param ids
	 * @return
	 */
	private int getElenmentInSegment(String member, int... ids){
		int segmentIndex = -1;//当前元素没有分片
		for(int i=0; i<SEGMENT_SIZE; i++){
			String segmentKey = buildKey(i, ids);
			if(score(segmentKey, member) > 0){
				segmentIndex = i;
				break;
			}
		}
		return segmentIndex;
	}

	@Override
	public double incrScore(String member, double delta, int... ids) {
		double score = getScore(member, ids);
		score += delta;
		add(member, score, ids);
		return score;
	}

	@Override
	public Set<String> findByScoreDesc(double min, double max, int... ids) {
		 Set<String> result = new LinkedHashSet<String>();
		 for(int i=0; i<SEGMENT_SIZE; i++){
				String segmentKey = buildKey(i, ids);
				long segmentElementSize = getSegmentElementSize(segmentKey);
				ZsetResult zsetResult = getSegmentLastElement(segmentKey, (int)segmentElementSize);
				if(zsetResult.getScore() == 0){
					break;
				}
				Set<String> set = stringRedisTemplate().opsForZSet().reverseRangeByScore(segmentKey, min, max);
				result.addAll(set);
				if(  min >= zsetResult.getScore()){
					break;
				}
		 }
		return result;
	}

	@Override
	public Set<String> findByScoreAsc(double min, double max, long offset, long count, int... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> findByScoreDesc(double min, double max, long offset, long count, int... ids) {
		 Set<String> result = new LinkedHashSet<String>();
		 for(int i=0; i<SEGMENT_SIZE; i++){
			 	String segmentKey = buildKey(i, ids);
				long segmentElementSize = getSegmentElementSize(segmentKey);
				ZsetResult zsetResult = getSegmentLastElement(segmentKey, (int)segmentElementSize);
				if(zsetResult.getScore() == 0){
					break;
				}
				Set<String> set = stringRedisTemplate().opsForZSet().reverseRangeByScore(segmentKey, min, max, offset, count);
				result.addAll(set);
				if(  min >= zsetResult.getScore()){
					break;
				}
		 }
		return result;
	}

	@Override
	public Set<String> findByScoreAsc(double min, double max, int... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> findByIdAsc(long start, long end, int... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> findByIdDesc(long start, long end, int... ids) {
		 Set<String> result = new LinkedHashSet<String>();
		 for(int i=0; i<SEGMENT_SIZE; i++){
			 String segmentKey = buildKey(i, ids);
			 long segmentElementSize = getSegmentElementSize(segmentKey);
			 long segmentSize = getSegmentSize(i-1);
			 if(segmentElementSize == 0){
				 break;
			 }
			 Set<String> set = stringRedisTemplate().opsForZSet().range(segmentKey, start-segmentSize, end);
			 if(set != null && set.size() > 0){
				 result.addAll(set);
			 }
			 if(segmentElementSize > end){
				 break;
			 }
			 
		 }
		return result;
	}

	@Override
	public List<ZsetResult> findByScoreWithScoresAsc(double min, double max, int... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ZsetResult> findByScoreWithScoresDesc(double min, double max, int... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ZsetResult> findByScoreWithScoresAsc(double min, double max, long offset, long count, int... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ZsetResult> findByScoreWithScoresDesc(double min, double max, long offset, long count, int... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getScore(String member, int... ids) {
		double score=0.0;
		for(int i=0; i<SEGMENT_SIZE; i++){
			String segmentKey = buildKey(i, ids);
			score = score(segmentKey, member);
			if(score > 0){
				break;
			}
		}
		return score;
	}

	@Override
	public boolean existMember(String member, int... ids) {
		boolean flag = false;
		for(int i=0; i<SEGMENT_SIZE; i++){
			String segmentKey = buildKey(i, ids);
			if(score(segmentKey, member) > 0){
				flag = true;
				break;
			}
		}
		return flag;
	}


	@Override
	public long size(int... ids) {
		long total = 0;
		for(int i=0; i<SEGMENT_SIZE; i++){
			String segmentKey = buildKey(i, ids);
			total += getSegmentElementSize(segmentKey);
		}
		return total;
	}

	@Override
	public long rangeSize(double min, double max, int... ids) {
		long total = 0;
		for(int i=0; i<SEGMENT_SIZE; i++){
			String segmentKey = buildKey(i, ids);
			long segmentElementSize = getSegmentElementSize(segmentKey);
			if(segmentElementSize == 0){
				break;
			}
			ZsetResult zsetResult = getSegmentLastElement(segmentKey, (int)segmentElementSize);
			if(zsetResult.getScore() <= 0){
				break;
			}
			total += stringRedisTemplate().opsForZSet().count(segmentKey, min, max);
			
			if(  min > zsetResult.getScore()){
				break;
			}
		}
		return total;
	}

	@Override
	public boolean remove(String member, int... ids) {
		//移除对应的元素
		for(int i=0; i<SEGMENT_SIZE; i++){
			String segmentKey = buildKey(i, ids);
			double score = score(segmentKey, member) ;
			if(score > 0){
				//移除对应的元素，并将后面的元素向前移动
				stringRedisTemplate().opsForZSet().remove(segmentKey, member);
				//移动元素
				long currSegmentSize = getSegmentElementSize(segmentKey);
				if(getSegmentSize(i) - currSegmentSize  > 0){
					forwardMoveSegment(i, (int)( getSegmentSize(i) - currSegmentSize ), segmentKey, ids);
				}
			}
		}
		return true;
	}


	@Override
	public boolean removeRange(long start, long end, int... ids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRangeByScore(double min, double max, int... ids) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 递归向后移动元素
	 * @param index
	 * @param maxSegmentSize
	 * @param segmentKey
	 */
	private void afterMoveSegment(int index, int maxSegmentSize, String segmentKey, int ... ids ){
		if(index < SEGMENT_SIZE-1){
			ZsetResult zsetResult = getSegmentLastElement(segmentKey, maxSegmentSize);
			List<ZsetResult> surplusElements = findSurplusElementsWithScoresDesc(0, zsetResult.getScore()-1, segmentKey);
			//将多余的元素加入到下一个分片中
			String nextSegmentKey = buildKey(index+1,ids);
			for(ZsetResult zr:surplusElements){
				stringRedisTemplate().opsForZSet().add(nextSegmentKey, zr.getValue(), zr.getScore());
				stringRedisTemplate().opsForZSet().remove(segmentKey, zr.getValue());
			}
			long nextSegmentElementSize = getSegmentElementSize(nextSegmentKey);
			long nextSegmentSize = getSegmentSize(index+1);
			if(nextSegmentSize < nextSegmentElementSize){
				//递归调用
				afterMoveSegment(index+1, (int)nextSegmentSize, nextSegmentKey, ids);
			}
		}
	}
	
	
	private double score(String segmentKey,String member){
		Double score = stringRedisTemplate().opsForZSet().score(segmentKey, member);
		if(score == null){
			return 0;
		}
		return score;
	}
	
	/**
	 * 递归向前移动元素
	 * @param index
	 * @param moveElementNum
	 * @param segmentKey
	 * @param ids
	 */
	private void forwardMoveSegment(int index, int moveElementNum, String segmentKey,int ... ids ){
		
		Preconditions.checkArgument(moveElementNum > 0, "moveElementNum need > 0");
		if(index < SEGMENT_SIZE -1){
			String nextSegmentKey = buildKey(index+1, ids);
			long nextSegmentSize = stringRedisTemplate().opsForZSet().size(nextSegmentKey);
			if(nextSegmentSize > 0){
				//需要移动数据
				Set<String> members = stringRedisTemplate().opsForZSet().reverseRange(nextSegmentKey, 0, moveElementNum-1);
				Preconditions.checkNotNull(members, "没可向前一个分片移动的数据");
				String currSegmentKey = buildKey(index, ids);
				for(String member:members){
					Double score = score(nextSegmentKey, member);
					stringRedisTemplate().opsForZSet().add(currSegmentKey, member, score);
					//移除
					stringRedisTemplate().opsForZSet().remove(nextSegmentKey, member);
				}
				//递归调用移动下一个
				forwardMoveSegment(index+1, moveElementNum, nextSegmentKey, ids);
			}
		}
	}
	
	/**
	 * 查询分片中多余的元素
	 * @param min
	 * @param max
	 * @param segmentKey
	 * @return
	 */
	private List<ZsetResult> findSurplusElementsWithScoresDesc(double min,double max, String segmentKey ){
		List<ZsetResult> zsetResults = new ArrayList<ZsetResult>();
		Set<TypedTuple<String>> results = stringRedisTemplate().opsForZSet().reverseRangeByScoreWithScores(segmentKey, min, max);
		for(TypedTuple<String>  tt: results){
			zsetResults.add(new ZsetResult(tt.getValue(), tt.getScore()));
		}
		results = null;
		return zsetResults;
	}
	
	/**
	 * 获取分片的最后一个数据
	 * @param segmentKey
	 * @param maxSegmentSize
	 * @return
	 */
	private ZsetResult getSegmentLastElement(String segmentKey,int maxSegmentSize){
		Set<String> set = stringRedisTemplate().opsForZSet().reverseRange(segmentKey, maxSegmentSize-1, maxSegmentSize-1);
		Preconditions.checkNotNull(set, "找不到最后一个分片数据");
		String member = "";
		for(String str:set){
			member = str;
		}
		double score = score(segmentKey, member);
		ZsetResult zsetResult = new ZsetResult(member, score);
		return zsetResult;
	}
	
	/**
	 * 获取分片有多少个元素
	 * @param segmentKey
	 * @return
	 */
	private long getSegmentElementSize(String segmentKey){
		return stringRedisTemplate().opsForZSet().size(segmentKey);
	}
	
	/**
	 * 构建分片的basekey
	 */
	private void buildSegmentBaseKey(){
	     key = new String [SEGMENT_SIZE];
	     for(int i=0;i<SEGMENT_SIZE;i++){
	    	 StringBuilder baseKeyBuilder = new StringBuilder(baseKey);
	    	 baseKeyBuilder.append(":").append(i).append(":").append("{0}");
	    	 key[i] = baseKeyBuilder.toString();
	     }
	}
	
	/**
	 * 构建key
	 * @param index
	 * @param ids
	 * @return
	 */
	private String buildKey(int index, int ... ids){
		StringBuilder builder = new StringBuilder();
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0;i < ids.length;i++ ){
			list.add(ids[i]);
		}
	     Joiner.on(":").appendTo(builder, list);
	     String segmentBaseKey = key[index];
	     return MessageFormat.format(segmentBaseKey, builder.toString());
	}
	
	/**
	 * 得到每个分片的长度
	 * @param index
	 * @return
	 */
	private int getSegmentSize(int index){
		if(index < 0){
			return 0;
		}
		if(index+1 == SEGMENT_SIZE){
			//最后一个Segment，无限大
			return -1;
		}
		int total = 1;
		for(int i=1;i<index+1;i++){
			total += i;
		}
		return total*SEGMENT_INITIAL_SIZE;
	}

	
}
