package org.simpleframework.aop;

import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.aop.annotation.Order;
import org.simpleframework.aop.aspect.DefaultAspect;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author malaka
 * @create 2020-12-14 17:18
 */
public class AspectWeaver {
    private BeanContainer beanContainer;

    public AspectWeaver() {
        this.beanContainer = BeanContainer.getInstance();
    }

//    这是aop1.0

//    public void doAop(){//真正开始织入逻辑
//        //1.获取所有的切面类(切面类：就是标记@Aspect注解的类，就是aop术语里的一个Aspect，里面有before、after方法等等)
//        Set<Class<?>> aspectSet = beanContainer.getClassesByAnnotation(Aspect.class);
//        //2.将切面类按照不同的织入目标进行切分，因为使用时 @Aspect 的value值可能是不同的，可能是Controller.class、Service.class等等
//        HashMap<Class<? extends Annotation>, List<AspectInfo>> categorizedMap = new HashMap<>();
//        if(ValidationUtil.isEmpty(aspectSet)) return;
//        for(Class<?> aspectClass : aspectSet){
//            if(verifyAspect(aspectClass)){ //verifyAspect()方法验证 切面类 的合法性
//                categorizeAspect(categorizedMap, aspectClass); // 对aspect进行分类(按照 @Aspect的value值进行分类)，分类进入categorizedMap 这个map里面
//            }else{  // 非法的切面类
//                throw new RuntimeException("@Aspect and @Order have not been added to the Aspect class," +
//                        "or Aspect class does not extend from DefaultAspect, or the value in Aspect Tag equals @Aspect");
//            }
//        }
//        //3.按照不同的织入目标分别去按序织入Aspect的逻辑
//        if(ValidationUtil.isEmpty(categorizedMap)) return; // 判空
//        for(Class<? extends Annotation> category: categorizedMap.keySet()){ // categorizedMap 里的key 就是要被织入的类，值就是对应的AspectInfo列表（AspectInfo里面集成了Order的值、Aspect切面类）
//            weaveByCategory(category, categorizedMap.get(category));
//        }
//    }
//
//    //3.按照不同的织入目标分别去按序织入Aspect的逻辑
//    private void weaveByCategory(Class<? extends Annotation> category, List<AspectInfo> aspectInfoList) {
//        //1.获取被代理类的集合
//        Set<Class<?>> classSet = beanContainer.getClassesByAnnotation(category);  // category是controller、service、repository、component之一，获取被这些标记的类
//        if (ValidationUtil.isEmpty(classSet)) return;
//        //2.遍历被代理类，分别为每个被代理类生成动态代理实例
//        for (Class<?> targetClass : classSet) {
//            //创建动态代理对象
//            AspectListExecutor aspectListExecutor = new AspectListExecutor(targetClass, aspectInfoList);
//            Object proxyBean = ProxyCreator.createProxy(targetClass, aspectListExecutor); // 这是写的工具类，背后利用cglib创建了动态代理；Enhancer.create
//            //3.将动态代理对象实例添加到容器里，取代未被代理前的类实例
//            beanContainer.addBean(targetClass, proxyBean);  // 在容器中，使用一个代理对象替换被代理对象
//        }
//    }
//
//    //2.将切面类(即DefaultAspect抽象类的子类)按照不同的织入目标（@Aspect的value值 ? ）进行分类；
//    // 这个方法会在for循环中被多次调用，多次调用后就分好类放入map里了    // 对aspect进行分类，分类进入categorizedMap 这个map里面
//    private void categorizeAspect(HashMap<Class<? extends Annotation>, List<AspectInfo>> categorizedMap, Class<?> aspectClass) { // aspectClass就是切面类，DefaultAspect抽象类的子类
//        Order orderTag = aspectClass.getAnnotation(Order.class); //得到切面类的Order标签
//        Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);  //得到切面类的Aspect标签
//        DefaultAspect aspect = (DefaultAspect) beanContainer.getBean(aspectClass);  // 从容器里获得 Aspect类 的bean实例
//        AspectInfo aspectInfo = new AspectInfo(orderTag.value(), aspect);
//        if (!categorizedMap.containsKey(aspectTag.value())) {   //Aspect注解的value是一个Class<? extends Annotation>
//            //如果织入的joinpoint第一次出现，则以该joinpoint为key，以新创建的List<Aspectinfo>为value ——> 因为一个被织入的类可能对应多个 切面类  // todo joinpoint 是啥？
//            List<AspectInfo> aspectInfoList = new ArrayList<>();
//            aspectInfoList.add(aspectInfo);
//            categorizedMap.put(aspectTag.value(), aspectInfoList);//Aspect注解的value是一个Class<? extends Annotation>
//        } else {
//            //如果织入的joinpoint不是第一次出现，则往joinpoint对应的value里添加新的Aspect逻辑
//            List<AspectInfo> aspectInfoList = categorizedMap.get(aspectTag.value());
//            aspectInfoList.add(aspectInfo);  // 直接把 aspect 加进 对应 aspect列表即可
//        }
//    }
//
//    //verifyAspect()方法验证条件如下：  即Aspect类标记有 @Aspect、@Order，且继承DefaultAspect，且 @Aspect的value值不能是Aspect.class
//    //框架中一定要遵守给Aspect类添加@Aspect和@Order标签的规范，同时，必须继承自DefaultAspect.class
//    //此外，@Aspect的属性值不能是它本身；   自己给自己织入就套娃死循环了，就不好
//    private boolean verifyAspect(Class<?> aspectClass) {
//        return aspectClass.isAnnotationPresent(Aspect.class) &&
//                aspectClass.isAnnotationPresent(Order.class) &&
//                DefaultAspect.class.isAssignableFrom(aspectClass) &&
//                aspectClass.getAnnotation(Aspect.class).value() != Aspect.class;
//    }

    //aop2.0，引入了aspectJ；复用AspectJ的解析能力
    public void doAop() {//真正开始织入逻辑
        //1.获取所有的切面类
        Set<Class<?>> aspectSet = beanContainer.getClassesByAnnotation(Aspect.class);
        if(ValidationUtil.isEmpty(aspectSet)) return;
        //2.拼装AspectInfoList；     初始化切面描述类集合
        List<AspectInfo> aspectInfoList = packAspectInfoList(aspectSet);

        //3.遍历容器里的类
        Set<Class<?>> classSet = beanContainer.getClasses(); // 得到容器内所有class集合
        for(Class<?> targetClass : classSet){
            //排除AspectClass自身；因为给Aspect做织入就死循环了，无限套娃。
            if(targetClass.isAnnotationPresent(Aspect.class)){
                continue;
            }
            //4.粗筛符合条件的Aspect 判断这些切面类的是否是为这个类服务的 留下每个类粗筛后的结果
            List<AspectInfo> roughMatchedAspectList = collectRoughMatchedAspectListForSpecificClass(aspectInfoList,targetClass);
            //5.尝试进行Aspect的织入      // 对每个类粗筛后的 尝试织入，后面还会进行方法级别的精筛
            wrapIfNecessary(roughMatchedAspectList, targetClass);
        }
    }

    /**2. 拼装AspectInfoList；拼接切面描述类List
     * @param aspectSet
     * @return
     */
    private List<AspectInfo> packAspectInfoList(Set<Class<?>> aspectSet) {
        List<AspectInfo> aspectInfoList = new ArrayList<>();  // 用于保存组装出来的AspectInfo
        for (Class<?> aspectClass : aspectSet) { // 遍历切面类(被Aspect标签标记的类集合的遍历)
            if (verifyAspect(aspectClass)) {
                // 1.获取aspectClass中的order和Aspect标签中的属性
                Order orderTag = aspectClass.getAnnotation(Order.class);
                Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
                // 2.创建PointcutLocator
                PointcutLocator pointcutLocator = new PointcutLocator(aspectTag.pointcut());
                // 3.从容器中获取切面类的实例
                DefaultAspect aspect = (DefaultAspect) beanContainer.getBean(aspectClass);
                // 4.创建AspectInfo封装类
                AspectInfo aspectInfo = new AspectInfo(orderTag.value(), aspect, pointcutLocator);
                aspectInfoList.add(aspectInfo);
            } else {
                throw new RuntimeException("@Aspect and @Order have not been added to the Aspect class, " +
                        "or Aspect class does not extend from DefaultAspect");
            }
        }
        return aspectInfoList;
    }

    /**
     * 4. 初筛符合条件的Aspect
     * @param aspectInfoList
     * @param targetClass
     * @return 返回初筛后的列表
     */
    private List<AspectInfo> collectRoughMatchedAspectListForSpecificClass(List<AspectInfo> aspectInfoList, Class<?> targetClass) {
        List<AspectInfo> roughMatchAspectList = new ArrayList<>();  // 用于保留粗筛后 保留的 切面类 的集合
        if (ValidationUtil.isEmpty(aspectInfoList)){return roughMatchAspectList;}
        for (AspectInfo aspectInfo : aspectInfoList) {
            //初筛 遍历容器里的class，看是否有匹配表达式的需要织入横切的类；
            // 该方法只对within表达式有效，对execution的表达式会直接返回true；故方法放只能 用于对类的`初步筛选`
            boolean matches = aspectInfo.getPointcutLocator().roughMatches(targetClass);
            //如果匹配 就加入
            if (matches){
                roughMatchAspectList.add(aspectInfo);
            }
        }
        return roughMatchAspectList;  // 这个粗筛出来的list可能是null，也可能 有多个
    }

    /**
     * 5. 尝试进行Aspect的织入
     * @param roughMatchedAspectList
     * @param targetClass
     */
    private void wrapIfNecessary(List<AspectInfo> roughMatchedAspectList, Class<?> targetClass) {
        // 创建动态代理对象
        AspectListExecutor executor = new AspectListExecutor(targetClass, roughMatchedAspectList); // 把粗筛列表通过AspectListExecutor的构造器传入
        Object proxyBean = ProxyCreator.createProxy(targetClass, executor); // 还是cglib的代理
        // 替换被代理的对象
        beanContainer.addBean(targetClass, proxyBean);
    }


    /**
     * 用来验证Aspect类是否满足自定义切面类的要求
     * 首先一定要遵守给Aspect类添加@Aspect标签和@Order标签的规范
     * 同时，一定要继承自DefaultAspect.class
     * 此外，@Aspect的属性不能是它本身  ——> 这一条好像没有体现
     *
     * @param aspectClass
     * @return
     */
    private boolean verifyAspect(Class<?> aspectClass) {
        /* //注解形式
        if (isDefaultAspect(aspectClass)){
            return aspectClass.isAnnotationPresent(Aspect.class)
                    && aspectClass.isAnnotationPresent(Order.class)
                    && DefaultAspect.class.isAssignableFrom(aspectClass)
                    && aspectClass.getAnnotation(Aspect.class).value() != Aspect.class;
        } else {
            //表达式形式
            return aspectClass.isAnnotationPresent(Aspect.class)
                    && aspectClass.isAnnotationPresent(Order.class)
                    && DefaultAspect.class.isAssignableFrom(aspectClass);
        }*/
        return aspectClass.isAnnotationPresent(Aspect.class) &&
                aspectClass.isAnnotationPresent(Order.class) &&
                DefaultAspect.class.isAssignableFrom(aspectClass); // isAssignableFrom 调用者是否是形参的超类、超接口，或本身
    }

    /**
     * 校验 切面类 遵循哪种规则
     * 是注解还是表达式
     * @param aspectClass
     * @return
     */
    private boolean isDefaultAspect(Class<?> aspectClass){
        if ( ! aspectClass.getAnnotation(Aspect.class).value().getName()
                .equals(DefaultAspect.class.getName())){  // 不相等，代表表达式形式
            return false;
        }else { // 相等，代表注解形式
            return true;
        }
    }



}
