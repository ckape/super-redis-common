package cn.friday.base.service.global.r.model;

import java.util.Date;

/**
 * @author Bin
 * @createTime 2013-4-20下午04:00:01
 * @lastEditTime 2013-4-20下午04:00:01
 * @copyright Design by Xtuone
 * @description 树洞微博原型
 */
public class TreeholeMessageRedis {
	private int id;
	private String content;
	private String imageUrl;
	private Date issueTime;
	private int studentId;
	private int schoolId;
	private int readCount;
	private String source;
	private int linkType;
	private String linkContent;
	// private boolean isProhibited;
	private int isProhibited;
	private int type;
	private int publisherType;
	private int weight;
	private int comments;
	private int support;
	private int hide;
	// 用来存储七牛图片相关信息
	private String qiniuImgInfo;

	private String gender;
	private String schoolName;
	/**
	 * 楼层总数
	 */
	private int floorTotal;

	// 对应板块
	private int topicId;

	// 是否匿名，0--匿名，1--实名
	private boolean isAnonymous;
	// 喜欢的个数
	private int likeCount;
	// 同步规则 0为同步到校内 1为不同步到校内
	private int syncRule;
	
	// 话题类型 0 为普通话题 2 为活动 3 打分 4 投票 5 夜聊
	private int category;
	// 语音信息 json
	private String voiceInfo;
	//投票选项
	private String voteOption;
	
	//总分，不保存到数据库，保存到redis中
	private int totalScore;
	
	private int myComments;

	public TreeholeMessageRedis() {
		super();
	}

	/**
	 * 包含七牛图片信息的构造方法
	 */
	public TreeholeMessageRedis(int id, String content, String imageUrl, Date issueTime, int studentId, int schoolId, int readCount, String source, int linkType,
			String linkContent, int isProhibited, int type, int publisherType, int weight, int comments, int support, int hide, String qiniuImgInfo,
			String gender, String schoolName) {
		super();
		this.id = id;
		this.content = content;
		this.imageUrl = imageUrl;
		this.issueTime = issueTime;
		this.studentId = studentId;
		this.schoolId = schoolId;
		this.readCount = readCount;
		this.source = source;
		this.linkType = linkType;
		this.linkContent = linkContent;
		this.isProhibited = isProhibited;
		this.type = type;
		this.publisherType = publisherType;
		this.weight = weight;
		this.comments = comments;
		this.support = support;
		this.hide = hide;
		this.qiniuImgInfo = qiniuImgInfo;
		this.gender = gender;
		this.schoolName = schoolName;
	}

	public TreeholeMessageRedis(int id, String content, String imageUrl, Date issueTime, int studentId, int schoolId, int readCount, String source, int linkType,
			String linkContent, int isProhibited, int type, int publisherType, int weight, int comments, int support, int hide) {
		super();
		this.id = id;
		this.content = content;
		this.imageUrl = imageUrl;
		this.issueTime = issueTime;
		this.studentId = studentId;
		this.schoolId = schoolId;
		this.readCount = readCount;
		this.source = source;
		this.linkType = linkType;
		this.linkContent = linkContent;
		this.isProhibited = isProhibited;
		this.type = type;
		this.publisherType = publisherType;
		this.weight = weight;
		this.comments = comments;
		this.support = support;
		this.hide = hide;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getLinkType() {
		return linkType;
	}

	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}

	public int getIsProhibited() {
		return isProhibited;
	}

	public void setIsProhibited(int isProhibited) {
		this.isProhibited = isProhibited;
	}

	public String getLinkContent() {
		return linkContent;
	}

	public void setLinkContent(String linkContent) {
		this.linkContent = linkContent;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPublisherType() {
		return publisherType;
	}

	public void setPublisherType(int publisherType) {
		this.publisherType = publisherType;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getSupport() {
		return support;
	}

	public void setSupport(int support) {
		this.support = support;
	}

	public int getHide() {
		return hide;
	}

	public void setHide(int hide) {
		this.hide = hide;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getQiniuImgInfo() {
		return qiniuImgInfo;
	}

	public void setQiniuImgInfo(String qiniuImgInfo) {
		this.qiniuImgInfo = qiniuImgInfo;
	}

	public int getFloorTotal() {
		return floorTotal;
	}

	public void setFloorTotal(int floorTotal) {
		this.floorTotal = floorTotal;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getSyncRule() {
		return syncRule;
	}

	public void setSyncRule(int syncRule) {
		this.syncRule = syncRule;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getVoiceInfo() {
		return voiceInfo;
	}

	public void setVoiceInfo(String voiceInfo) {
		this.voiceInfo = voiceInfo;
	}
	public String getVoteOption() {
		return voteOption;
	}
	
	public void setVoteOption(String voteOption) {
		this.voteOption = voteOption;
	}
	
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getMyComments() {
		return myComments;
	}

	public void setMyComments(int myComments) {
		this.myComments = myComments;
	}

	@Override
	public String toString() {
		return "TreeholeMessage [id=" + id + ", content=" + content + ", imageUrl=" + imageUrl + ", issueTime=" + issueTime + ", studentId=" + studentId
				+ ", schoolId=" + schoolId + ", readCount=" + readCount + ", source=" + source + ", linkType=" + linkType + ", linkContent=" + linkContent
				+ ", isProhibited=" + isProhibited + ", type=" + type + ", publisherType=" + publisherType + ", weight=" + weight + ", comments=" + comments
				+ ", support=" + support + ", hide=" + hide + ", qiniuImgInfo=" + qiniuImgInfo + ", gender=" + gender + ", schoolName=" + schoolName
				+ ", floorTotal=" + floorTotal + ", topicId=" + topicId + ", isAnonymous=" + isAnonymous + ", likeCount=" + likeCount + ", syncRule="
				+ syncRule + ", category=" + category + ", voiceInfo=" + voiceInfo + ", voteOption=" + voteOption + ", totalScore=" + totalScore + "]";
	}

}
