"""Convert Bedrock geometry JSON block models to Java block model JSON.

The converter keeps Bedrock's pixel UV layout by scaling UV coordinates to
Minecraft Java's 0..16 model UV space. Bedrock block origins are centered on
the block, so X/Z coordinates are translated by +8.
"""

from __future__ import annotations

import json
import math
from pathlib import Path
from typing import Any


def _scaled_uv(value: float, texture_size: float) -> float:
    return round(value * 16.0 / texture_size, 6)


def _face_uv(face: dict[str, Any], width: int, height: int) -> list[float]:
    uv = face.get("uv", [0, 0])
    size = face.get("uv_size", [0, 0])
    return [
        _scaled_uv(uv[0], width),
        _scaled_uv(uv[1], height),
        _scaled_uv(uv[0] + size[0], width),
        _scaled_uv(uv[1] + size[1], height),
    ]


def _rotation(cube: dict[str, Any], bone_rotation: list[float] | None, bone_pivot: list[float] | None) -> dict[str, Any] | None:
    rotation = cube.get("rotation")
    pivot = cube.get("pivot")
    if rotation is None and bone_rotation is not None:
        rotation = bone_rotation
        pivot = bone_pivot
    if rotation is None or pivot is None:
        return None

    values = [float(v) for v in rotation]
    nonzero = [(axis, angle) for axis, angle in zip(("x", "y", "z"), values) if abs(angle) > 1e-6]
    if len(nonzero) != 1:
        # Java block elements support one rotation axis. Preserve the most
        # significant axis instead of silently dropping all rotation.
        axis, angle = max(zip(("x", "y", "z"), values), key=lambda item: abs(item[1]))
    else:
        axis, angle = nonzero[0]
    return {
        "origin": [round(float(pivot[0]) + 8, 6), round(float(pivot[1]), 6), round(float(pivot[2]) + 8, 6)],
        "axis": axis,
        "angle": round(-float(angle), 6),
        "rescale": False,
    }


def convert(source: Path, output: Path, texture: str) -> None:
    data = json.loads(source.read_text(encoding="utf-8"))
    geometry = data["minecraft:geometry"][0]
    description = geometry["description"]
    width = int(description.get("texture_width", 16))
    height = int(description.get("texture_height", 16))
    elements: list[dict[str, Any]] = []

    for bone in geometry.get("bones", []):
        bone_rotation = bone.get("rotation")
        bone_pivot = bone.get("pivot")
        for cube in bone.get("cubes", []):
            origin = cube["origin"]
            size = cube["size"]
            element: dict[str, Any] = {
                "from": [round(float(origin[0]) + 8, 6), round(float(origin[1]), 6), round(float(origin[2]) + 8, 6)],
                "to": [
                    round(float(origin[0]) + float(size[0]) + 8, 6),
                    round(float(origin[1]) + float(size[1]), 6),
                    round(float(origin[2]) + float(size[2]) + 8, 6),
                ],
                "faces": {},
            }
            rotation = _rotation(cube, bone_rotation, bone_pivot)
            if rotation is not None:
                element["rotation"] = rotation

            uv = cube.get("uv", [0, 0])
            if isinstance(uv, list):
                # Bedrock's shorthand UV is the standard cube layout. Use
                # the full texture for each face when no per-face UV exists.
                faces = {side: {"uv": [0, 0, 16, 16]} for side in ("down", "up", "north", "south", "west", "east")}
            else:
                faces = {side: {"uv": _face_uv(face, width, height)} for side, face in uv.items()}
            for face in faces.values():
                face["texture"] = "#texture"
            element["faces"] = faces
            elements.append(element)

    model = {
        "parent": "minecraft:block/block",
        "textures": {"particle": texture, "texture": texture},
        "elements": elements,
    }
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text(json.dumps(model, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


if __name__ == "__main__":
    import argparse

    parser = argparse.ArgumentParser()
    parser.add_argument("source", type=Path)
    parser.add_argument("output", type=Path)
    parser.add_argument("texture")
    args = parser.parse_args()
    convert(args.source, args.output, args.texture)
