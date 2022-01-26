package com.sso.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sso.common.constants.SerialNumConstants;
import com.sso.common.entity.WmsSerialNumber;
import com.sso.common.mapper.WmsSerialNumberMapper;
import com.sso.service.IWmsSerialNumberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-01-23
 */
@Service
public class WmsSerialNumberServiceImpl extends ServiceImpl<WmsSerialNumberMapper, WmsSerialNumber> implements IWmsSerialNumberService {

    /** 格式 */
    private String pattern = "";

    /** 生成器锁 */
    private final ReentrantLock lock = new ReentrantLock();

    /** 流水号格式化器 */
    private DecimalFormat format = null;

    /** 预生成锁 */
    private final ReentrantLock prepareLock = new ReentrantLock();

    /** 最小值 */
    private int min = 0;

    /** 最大值 */
    private long max = 0;

    /** 已生成流水号（种子） */
    private long seed = min;

    /** 预生成数量 */
    private int prepare = 0;

    /** 数据库存储的当前最大序列号 **/
    long maxSerialInt = 0;

    WmsSerialNumber serialNumber=new WmsSerialNumber();

    /** 当前序列号是否为个位数自增的模式 **/
    private String isAutoIncrement = "0";

    /** 预生成流水号 */
    HashMap<String, List<String>> prepareSerialNumberMap = new HashMap<>();
    @Autowired
    private WmsSerialNumberMapper wmsSerialNumberMapper;

    /**
     * 根据模块code生成预数量的序列号存放到Map中
     * @param moduleCode 模块code
     * @return
     */
    @CachePut(value = "serialNumber",key="#moduleCode")
    public List<String> generatePrepareSerialNumbers(String moduleCode){
        //临时List变量
        List<String> resultList = new ArrayList<String>(prepare);
        lock.lock();
        try{
            for(int i=0;i<prepare;i++){
                maxSerialInt  = maxSerialInt + 1;
                if(maxSerialInt > min && (maxSerialInt + "").length() < max ){
                    seed = maxSerialInt ;
                }else{
                    //如果动态数字长度大于模板中的长度 例：模板CF000  maxSerialInt 1000
                    seed = maxSerialInt = 0;
                    //更新数据，重置maxSerialInt为0
                    serialNumber.setMaxSerial("0");
                    wmsSerialNumberMapper.updateById(serialNumber);
                }
                //动态数字生成
                String formatSerialNum = format.format(seed);

                //动态日期的生成
                if(pattern.contains(SerialNumConstants.DATE_SYMBOL)){
                    String currentDate = DateUtil.format(new Date(),"yyyyMMdd");
                    formatSerialNum = formatSerialNum.replace(SerialNumConstants.DATE_SYMBOL,currentDate);
                }

                resultList.add(formatSerialNum);
            }
            //更新数据
            serialNumber.setMaxSerial(maxSerialInt + "");
            wmsSerialNumberMapper.updateById(serialNumber);
        }finally{
            lock.unlock();
        }
        return resultList;
    }

    /**
     * 根据模块code生成序列号
     * @param moduleCode  模块code
     * @return  序列号
     */
    public String generateSerialNumberByModelCode(String moduleCode){

        //预序列号加锁
        prepareLock.lock();
        try{
            //判断内存中是否还有序列号
            if(null != prepareSerialNumberMap.get(moduleCode) && prepareSerialNumberMap.get(moduleCode).size() > 0){
                //若有，返回第一个，并删除
                return prepareSerialNumberMap.get(moduleCode).remove(0);
            }
        }finally {
            //预序列号解锁
            prepareLock.unlock();
        }
        QueryWrapper<WmsSerialNumber> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("module_code",moduleCode);
        serialNumber = wmsSerialNumberMapper.selectOne(queryWrapper);
        prepare = Integer.parseInt(serialNumber.getPreMaxNum().trim());//预生成流水号数量
        pattern = serialNumber.getConfigTemplet().trim();//配置模板
        String maxSerial = serialNumber.getMaxSerial().trim(); //存储当前最大值
        isAutoIncrement = serialNumber.getIsAutoIncrement().trim();
        maxSerialInt = Long.parseLong(maxSerial.trim());//数据库存储的最大序列号
        max = this.counter(pattern,'0') + 1;//根据模板判断当前序列号数字的最大值
        if(isAutoIncrement.equals("1")){
            pattern = pattern.replace("0","#");
        }
        format = new DecimalFormat(pattern);
        //生成预序列号，存到缓存中
        List<String> resultList = generatePrepareSerialNumbers(moduleCode);
        prepareLock.lock();
        try {
            prepareSerialNumberMap.put(moduleCode, resultList);
            return prepareSerialNumberMap.get(moduleCode).remove(0);
        } finally {
            prepareLock.unlock();
        }
    }

    /**
     * 设置最小值
     *
     * @param value 最小值，要求：大于等于零
     * @return 流水号生成器实例
     */
    public IWmsSerialNumberService setMin(int value) {
        lock.lock();
        try {
            this.min = value;
        }finally {
            lock.unlock();
        }
        return this;
    }

    /**
     * 最大值
     *
     * @param value 最大值，要求：小于等于Long.MAX_VALUE ( 9223372036854775807 )
     * @return 流水号生成器实例
     */
    public IWmsSerialNumberService setMax(long value) {
        lock.lock();
        try {
            this.max = value;
        }finally {
            lock.unlock();
        }
        return this;
    }

    /**
     * 设置预生成流水号数量
     * @param count 预生成数量
     * @return      流水号生成器实例
     */
    public IWmsSerialNumberService setPrepare(int count) {
        lock.lock();
        try {
            this.prepare = count;
        }finally {
            lock.unlock();
        }
        return this;
    }

    /**
     * 统计某一个字符出现的次数
     * @param str 查找的字符
     * @param c
     * @return
     */
    private int counter(String str,char c){
        int count=0;
        for(int i = 0;i < str.length();i++){
            if(str.charAt(i)==c){
                count++;
            }
        }
        return count;
    }
}
