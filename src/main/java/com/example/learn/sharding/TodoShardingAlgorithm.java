package com.example.learn.sharding;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class TodoShardingAlgorithm implements StandardShardingAlgorithm<Comparable<?>> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Comparable<?>> shardingValue) {
        // availableTargetNames: 配置文件里定义的实际表名集合 [todo_0, todo_1]
        // shardingValue: 当前 SQL 中的分片键值 (user_id)

        Long userId = (Long) shardingValue.getValue();
        
        // 特殊处理：如果 userId 为 null (虽然 PreciseShardingValue 通常不会传 null，但在某些场景或自定义封装下可能)
        // 注意：ShardingSphere 在 SQL 解析阶段如果发现没传分片键，可能根本不会进到这里，而是报错。
        // 但如果我们传了 userId = 0 或其他特定值，可以在这里处理。
        // 对于真正的 null 值，业务层通常需要给默认值。这里演示的是“编程式控制”的能力。
        
        if (userId == null) {
            // 兜底逻辑：去 todo_0
            for (String tableName : availableTargetNames) {
                if (tableName.endsWith("_0")) {
                    return tableName;
                }
            }
        }

        // 标准取模逻辑
        long suffix = userId % 2;
        String suffixStr = String.valueOf(suffix);

        for (String tableName : availableTargetNames) {
            if (tableName.endsWith("_" + suffixStr)) {
                return tableName;
            }
        }

        throw new UnsupportedOperationException("无法路由到目标表，userId: " + userId);
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Comparable<?>> shardingValue) {
        Set<String> result = new HashSet<>();
        Range<Comparable<?>> range = shardingValue.getValueRange();

        // 1. 如果范围没有下界或没有上界 (比如 user_id > 100)，无法确定具体区间，为了安全，必须全表扫描
        if (!range.hasLowerBound() || !range.hasUpperBound()) {
            return availableTargetNames;
        }

        long lower = (Long) range.lowerEndpoint();
        long upper = (Long) range.upperEndpoint();

        // 2. 处理开闭区间 (Open: 不包含, Closed: 包含)
        if (range.lowerBoundType() == BoundType.OPEN) {
            lower++;
        }
        if (range.upperBoundType() == BoundType.OPEN) {
            upper--;
        }

        // 3. 性能优化：如果区间跨度 >= 分表数 (这里是2)，那肯定覆盖了所有表，直接返回全表
        // 这样可以避免 range 很大时循环计算消耗 CPU
        if (upper - lower >= availableTargetNames.size()) {
            return availableTargetNames;
        }

        // 4. 精确循环计算
        for (long i = lower; i <= upper; i++) {
            long suffix = i % 2; // 这里必须与 Precise 里的逻辑保持一致
            for (String tableName : availableTargetNames) {
                if (tableName.endsWith("_" + suffix)) {
                    result.add(tableName);
                }
            }
            // 只要找齐了所有表，就提前退出
            if (result.size() == availableTargetNames.size()) {
                return availableTargetNames;
            }
        }
        return result;
    }

    @Override
    public void init(Properties properties) {
        // 可以从 yaml 读取自定义属性
    }

    @Override
    public String getType() {
        return "CUSTOM_TODO"; // 给这个算法起个名字，SPI 机制会用到
    }

    @Override
    public Properties getProps() {
        return new Properties();
    }
}
