import java.lang.reflect.Field;

/**
 * @author chenzufeng
 * @date 2021/7/25
 * @usage FruitInfoUtil 注解处理器
 */
public class FruitInfoUtil {
    public static void getFruitInfo(Class<?> clazz) {
        String fruitNameStr = "水果名称：";
        String fruitColorStr = "水果颜色：";
        String fruitProviderStr = "供应商信息：";

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 含有 FruitName 注解
            if (field.isAnnotationPresent(FruitName.class)) {
                FruitName fruitName = field.getAnnotation(FruitName.class);
                // 如果使用 + fruitName 则输出 @FruitName(value=Apple)
                fruitNameStr = fruitNameStr + fruitName.value();
                System.out.println(fruitNameStr);
            } else if (field.isAnnotationPresent(FruitColor.class)) {
                FruitColor fruitColor = field.getAnnotation(FruitColor.class);
                fruitColorStr = fruitColorStr + fruitColor.fruitColor();
                System.out.println(fruitColorStr);
            } else if (field.isAnnotationPresent(FruitProvider.class)) {
                FruitProvider fruitProvider = field.getAnnotation(FruitProvider.class);
                fruitProviderStr = "供应商编号：" + fruitProvider.id() + "；"
                        + "供应商名称：" + fruitProvider.name() + "；"
                        + "供应商地址：" + fruitProvider.address() + "。";
                System.out.println(fruitProviderStr);
            }
        }
    }
}
