"""
Author       : FYWinds i@windis.cn
Date         : 2024-05-14 17:32:56
LastEditors  : FYWinds i@windis.cn
LastEditTime : 2024-05-14 20:55:01
FilePath     : /extract_cooldown_list.py
"""

import json
import re

COOLDOWN_REGEX = re.compile(r"\((.*\.CHARM_COOLDOWN),")
CAPITAL_SPLIT_REGEX = re.compile(r".[^A-Z]*")

with open("orig_file", "r", encoding="utf-8") as f:
    data = f.readlines()

cooldown_map = {}

for line in data:
    if result := COOLDOWN_REGEX.search(line):
        cooldown = result.group(1)
        cooldown_map[cooldown] = (
            " ".join(
                CAPITAL_SPLIT_REGEX.findall(
                    cooldown.removeprefix("Depths").removesuffix(".CHARM_COOLDOWN")
                )
            )
            + " Cooldown"
        ).replace("Of", "of")


with open("cooldown_mapping.json", "w", encoding="utf-8") as f:
    json.dump(cooldown_map, f, indent=4, ensure_ascii=False)
