"""
Author       : FYWinds i@windis.cn
Date         : 2024-05-14 17:44:21
LastEditors  : FYWinds i@windis.cn
LastEditTime : 2024-05-14 18:32:52
FilePath     : /process.py
"""

import json
import re

DATA_EXTRCT_REGEX = re.compile(
    r"""\("?([\w\s]+(?:\.CHARM_COOLDOWN)?)"?, .*?, (true|false), (true|false), ([-\d.]+), ([-\d.]+), .*?{([-\d.]+), ([-\d.]+), ([-\d.]+), ([-\d.]+), ([-\d.]+)}\)"""
)

processed = {
    "version": "1.0.0",
    "data": {},
}

with open("orig_file", "r", encoding="utf-8") as f:
    data = f.readlines()

with open("cooldown_mapping.json", "r", encoding="utf-8") as f:
    name_map = json.load(f)


def zeroClamp(value: float, cap: float) -> float:
    if cap == 0:
        return value
    if cap < 0:
        return min(max(cap, value), 0)
    else:
        return max(min(cap, value), 0)


def getStat(value: float, variance: float, cap: float) -> list:
    return [
        round(zeroClamp(value - variance, cap), 2),
        round(zeroClamp(value + variance, cap), 2),
    ]


for line in data:
    if result := DATA_EXTRCT_REGEX.search(line):
        (
            name,
            isOnlyPositive,
            isPercent,
            variance,
            effectCap,
            commonVal,
            uncommonVal,
            rareVal,
            epicVal,
            legendaryVal,
        ) = result.groups()

        (
            variance,
            effectCap,
            commonVal,
            uncommonVal,
            rareVal,
            epicVal,
            legendaryVal,
        ) = map(
            float,
            (
                variance,
                effectCap,
                commonVal,
                uncommonVal,
                rareVal,
                epicVal,
                legendaryVal,
            ),
        )

        name = name_map.get(name, name)  # Special case for the constants COOLDOWN

        processed["data"][name] = {
            "isOnlyPositive": isOnlyPositive == "true",
            "isPercent": isPercent == "true",
            "effectCap": float(effectCap),
            "commonStat": getStat(commonVal, variance, effectCap),
            "uncommonStat": getStat(uncommonVal, variance, effectCap),
            "rareStat": getStat(rareVal, variance, effectCap),
            "epicStat": getStat(epicVal, variance, effectCap),
            "legendaryStat": getStat(legendaryVal, variance, effectCap),
        }


json.dump(processed, open("Zenithcharm_stats.json", "w", encoding="utf-8"), indent=4)
json.dump(
    processed,
    open("Zenithcharm_stats.min.json", "w", encoding="utf-8"),
    separators=(",", ":"),
    ensure_ascii=False,
)

# Dump to mod data folder
json.dump(
    processed,
    open(
        "../src/main/resources/assets/monutils/data/Zenithcharm_stats.json",
        "w",
        encoding="utf-8",
    ),
    separators=(",", ":"),
    ensure_ascii=False,
)
