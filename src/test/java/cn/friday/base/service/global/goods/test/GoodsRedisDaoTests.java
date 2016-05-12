package cn.friday.base.service.global.goods.test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.friday.base.service.global.Application;
import cn.friday.base.service.global.goods.dao.GoodsRedisDao;
import cn.friday.base.service.global.goods.model.GoodsRedis;
import cn.friday.base.service.global.redis.loader.RedisLoader;
import cn.friday.base.service.global.redis.syncer.Syncer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class GoodsRedisDaoTests {

	@Autowired
	GoodsRedisDao goodsRedisDao;

	@Test
	public void test() {
		System.out.println("test-----------");
		GoodsRedis goodsRedis = new GoodsRedis();
		goodsRedisDao.save(createGoods(), new Syncer<GoodsRedis>() {

			@Override
			public void excute(GoodsRedis t) {
				System.out.println("sync data:" + t);

			}
		});
	}

	@Test
	public void findById() {
		for (int i = 0; i < 5; i++) {
			GoodsRedis gr = goodsRedisDao.findById(i, new GoodsRedisLoader(i));
			System.out.println("dd:" + gr);
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testFindByProperty() {
		for (int i = 0; i < 5; i++) {
			System.out.println(goodsRedisDao.findByProperty("title", i, new GoodsRedisLoader(i)));
		}
	}

	private GoodsRedis createGoods() {
		GoodsRedis goodsRedis = new GoodsRedis();
		goodsRedis.setName("name" + System.currentTimeMillis());
		goodsRedis.setTitle("title1");
		goodsRedis.setAddTime(new Date());
		return goodsRedis;
	}

	class GoodsRedisLoader extends RedisLoader<GoodsRedis> {

		private int id;

		public GoodsRedisLoader(int id) {
			this.id = id;
		}

		@Override
		public GoodsRedis load() {
			GoodsRedis goodsRedis = new GoodsRedis();
			goodsRedis.setAddTime(new Date());
			goodsRedis.setId(id);
			goodsRedis.setName("name-------backup:" + id);
			goodsRedis.setTitle("title--backup:" + id);
			return goodsRedis;
		}

		@Override
		public boolean isNeedCache(GoodsRedis t) {
			return System.currentTimeMillis() - t.getAddTime().getTime() < 86400000;
		}

		@Override
		public int expire(GoodsRedis t) {
			return 0;
		}
	}

}
