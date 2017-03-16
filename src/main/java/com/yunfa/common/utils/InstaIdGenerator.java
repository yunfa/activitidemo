package com.yunfa.common.utils;

/**
 * @author yunfa
 * @version 1.0
 * @date 2017-01-05.
 */
public class InstaIdGenerator {

	/**
	 * 鏃堕棿鎴崇殑浣嶆暟锛屽疄闄呭崰41浣嶏紝鏈�楂樹綅淇濇寔涓�0锛屼繚璇乴ong鍊间负姝ｆ暟
	 */
	private int timestampBitCount = 42;

	/**
	 * 閫昏緫鍒嗙墖浣嶆暟
	 */
	private int regionBitCount = 10;

	/**
	 * 閫昏緫鍒嗙墖鐨勬渶澶ф暟閲�
	 */
	private int regionModelVal = 1 << regionBitCount;

	/**
	 * 搴忓垪浣嶆暟
	 */
	private int sequenceBitCount = 12;

	/**
	 * 鎬荤殑浣嶆暟
	 */
	private int totalBitCount = timestampBitCount + regionBitCount + sequenceBitCount;

	/**
	 * 褰撳墠搴忓垪鍊�
	 */
	private long sequence = 0;

	/**
	 * 鏈�鍚庝竴娆¤姹傛椂闂存埑
	 */
	private long lastTimestamp = -1L;

	/**
	 * 搴忓垪鐨勪綅鏉�
	 */
	private long sequenceMask = -1L ^ (-1L << sequenceBitCount);

	/**
	 * 鏈�鍚庝竴娆¤姹傜敤鎴锋爣璇�
	 */
	private long lastTag = 1L;

	public InstaIdGenerator() {}

	public InstaIdGenerator(long seq) {
		if(seq < 0) {
			seq = 0;
		}
		this.sequence = seq;
	}

	public synchronized long nextId(long tag) {
		long timestamp = timeGen();
		if(tag < 0) {
			tag = -tag;
		}
		if(timestamp < lastTimestamp) {
			// LOG.error(String.format("clock is moving backwards.  Rejecting requests until %d.",
			// lastTimestamp));
			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}
		if(lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if(sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}
		if(tag == lastTag) {
			sequence = (sequence + 4) & sequenceMask;
			if(sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		}
		lastTag = tag;
		lastTimestamp = timestamp;
		return (timestamp << (totalBitCount - timestampBitCount))
				| ((tag % regionModelVal) << (totalBitCount - timestampBitCount - regionBitCount)) | sequence;
	}

	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while(timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected long timeGen() {
		return System.currentTimeMillis();
	}

	public static void main(String[] args) {
		InstaIdGenerator InstaIdGenerator = new InstaIdGenerator(120);
		for(int i = 0; i < 10; i++) {
			long id = InstaIdGenerator.nextId(12);
			System.out.println(id);
		}
	}
}
