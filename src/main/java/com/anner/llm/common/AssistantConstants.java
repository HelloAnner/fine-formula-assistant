package com.anner.llm.common;

import java.time.Duration;
import java.util.List;

/**
 * @author Anner
 * @since 12.0
 *        Created on 2025/3/12
 */
public class AssistantConstants {
    public final static String EMBED_JSON_FILE_NAME = "embeddingStore.json";

    public final static int MAX_RETRIES = 10;

    public final static int MAX_TOKENS = 100000;

    public final static double TEMPERATURE = 0.5;

    public final static double TOP_P = 0.9;

    public final static Duration TIMEOUT = Duration.ofSeconds(600);

    public final static List<String> DOCUMENT_URLS = List.of(
            "https://help.fanruan.com/finereport/doc-view-4244.html",
            "https://help.fanruan.com/finereport/doc-view-253.html",
            "https://help.fanruan.com/finereport/doc-view-249.html",
            "https://help.fanruan.com/finereport/doc-view-252.html",
            "https://help.fanruan.com/finereport/doc-view-3930.html",
            "https://help.fanruan.com/finereport/doc-view-831.html",
            "https://help.fanruan.com/finereport/doc-view-869.html",
            "https://help.fanruan.com/finereport/doc-view-867.html",
            "https://help.fanruan.com/finereport/doc-view-827.html",
            "https://help.fanruan.com/finereport/doc-view-840.html",
            "https://help.fanruan.com/finereport/doc-view-3598.html",
            "https://help.fanruan.com/finereport/doc-view-824.html",
            "https://help.fanruan.com/finereport/doc-view-1897.html",
            "https://help.fanruan.com/finereport/doc-view-880.html",
            "https://help.fanruan.com/finereport/doc-view-4456.html",
            "https://help.fanruan.com/finereport/doc-view-843.html",
            "https://help.fanruan.com/finereport/doc-view-1903.html",
            "https://help.fanruan.com/finereport/doc-view-853.html",
            "https://help.fanruan.com/finereport/doc-view-846.html",
            "https://help.fanruan.com/finereport/doc-view-839.html",
            "https://help.fanruan.com/finereport/doc-view-3637.html",
            "https://help.fanruan.com/finereport/doc-view-810.html",
            "https://help.fanruan.com/finereport/doc-view-854.html",
            "https://help.fanruan.com/finereport/doc-view-842.html",
            "https://help.fanruan.com/finereport/doc-view-841.html",
            "https://help.fanruan.com/finereport/doc-view-838.html",
            "https://help.fanruan.com/finereport/doc-view-850.html",
            "https://help.fanruan.com/finereport/doc-view-802.html",
            "https://help.fanruan.com/finereport/doc-view-4583.html",
            "https://help.fanruan.com/finereport/doc-view-4580.html",
            "https://help.fanruan.com/finereport/doc-view-4581.html",
            "https://help.fanruan.com/finereport/doc-view-4582.html",
            "https://help.fanruan.com/finereport/doc-view-4585.html",
            "https://help.fanruan.com/finereport/doc-view-4584.html",
            "https://help.fanruan.com/finereport/doc-view-1493.html",
            "https://help.fanruan.com/finereport/doc-view-3624.html",
            "https://help.fanruan.com/finereport/doc-view-4133.html",
            "https://help.fanruan.com/finereport/doc-view-4454.html",
            "https://help.fanruan.com/finereport/doc-view-4710.html",
            "https://help.fanruan.com/finereport/doc-view-4732.html",
            "https://help.fanruan.com/finereport/doc-view-4869.html",
            "https://help.fanruan.com/finereport/doc-view-4870.html",
            "https://help.fanruan.com/finereport/doc-view-4871.html",
            "https://help.fanruan.com/finereport/doc-view-4872.html",
            "https://help.fanruan.com/finereport/doc-view-4873.html",
            "https://help.fanruan.com/finereport/doc-view-4874.html",
            "https://help.fanruan.com/finereport/doc-view-4875.html",
            "https://help.fanruan.com/finereport/doc-view-5040.html",
            "https://help.fanruan.com/finereport/doc-view-5096.html",
            "https://help.fanruan.com/finereport/doc-view-5456.html",
            "https://help.fanruan.com/finereport/doc-view-3373.html",
            "https://help.fanruan.com/finereport/doc-view-813.html#",
            "https://help.fanruan.com/finereport/doc-view-816.html#",
            "https://help.fanruan.com/finereport/doc-view-817.html#",
            "https://help.fanruan.com/finereport/doc-view-819.html#",
            "https://help.fanruan.com/finereport/doc-view-824.html#",
            "https://help.fanruan.com/finereport/doc-view-3598.html#",
            "https://help.fanruan.com/finereport/doc-view-827.html#",
            "https://help.fanruan.com/finereport/doc-view-1785.html",
            "https://help.fanruan.com/finereport/doc-view-810.html",
            "https://help.fanruan.com/finereport/doc-view-854.html",
            "https://help.fanruan.com/finereport/doc-view-842.html",
            "https://help.fanruan.com/finereport/doc-view-841.html",
            "https://help.fanruan.com/finereport/doc-view-838.html",
            // // 层次坐标公式
            "https://help.fanruan.com/finereport/doc-view-4583.html",
            "https://help.fanruan.com/finereport/doc-view-4580.html",
            "https://help.fanruan.com/finereport/doc-view-4581.html",
            "https://help.fanruan.com/finereport/doc-view-4582.html",
            "https://help.fanruan.com/finereport/doc-view-4585.html",
            "https://help.fanruan.com/finereport/doc-view-4584.html",
            // // 函数应用
            "https://help.fanruan.com/finereport/doc-view-2503.html",
            "https://help.fanruan.com/finereport/doc-view-3741.html",
            "https://help.fanruan.com/finereport/doc-view-3764.html",
            "https://help.fanruan.com/finereport/doc-view-3945.html",
            "https://help.fanruan.com/finereport/doc-view-4233.html",
            "https://help.fanruan.com/finereport/doc-view-2363.html",
            "https://help.fanruan.com/finereport/doc-view-2364.html",
            "https://help.fanruan.com/finereport/doc-view-2382.html",
            "https://help.fanruan.com/finereport/doc-view-2383.html",
            "https://help.fanruan.com/finereport/doc-view-2388.html",
            "https://help.fanruan.com/finereport/doc-view-3549.html",
            "https://help.fanruan.com/finereport/doc-view-5460.html",
            "https://help.fanruan.com/finereport/doc-view-878.html",
            "https://help.fanruan.com/finereport/doc-view-847.html");

    public final static int BATCH_SIZE = 200;
}
