#!/usr/bin/env python3
import sys
import os
import urllib.request
import urllib.error
import re
import datetime
import html

# --- Configuration ---
YEAR = 2025
BASE_URL = "https://adventofcode.com"
USER_AGENT = "github.com/bakh/adventofcode by [user email/contact if known, otherwise generic]"

# Paths
JAVA_BASE_PATH = "src/main/java/org/bakh/adventofcode"
JAVA_TEST_PATH = "src/test/java/org/bakh/adventofcode"
RESOURCE_BASE_PATH = "src/main/resources"
RESOURCE_TEST_PATH = "src/test/resources"

def get_session_cookie():
    if os.path.exists(".session"):
        with open(".session", "r") as f:
            return f.read().strip()

    if "AOC_SESSION" in os.environ:
        return os.environ["AOC_SESSION"]

    print("Session cookie not found in .session file or AOC_SESSION env var.")
    cookie = input("Please paste your Advent of Code session cookie: ").strip()

    save = input("Save this cookie to .session? (y/n) [y]: ").strip().lower()
    if save in ["", "y", "yes"]:
        with open(".session", "w") as f:
            f.write(cookie)
            print("Cookie saved to .session")

    return cookie

def fetch_url(url, cookie=None):
    req = urllib.request.Request(url)
    if cookie:
        req.add_header("Cookie", f"session={cookie}")
    req.add_header("User-Agent", USER_AGENT)

    try:
        with urllib.request.urlopen(req) as response:
            return response.read().decode('utf-8')
    except urllib.error.HTTPError as e:
        print(f"Error fetching {url}: {e}")
        if e.code == 404:
            print("Puzzle not available yet or invalid day.")
        elif e.code == 400:
             print("Bad request. Auth might be missing for input.")
        sys.exit(1)

def parse_title(html_content):
    title_match = re.search(r'<h2>--- Day \d+: (.+) ---</h2>', html_content)
    title = title_match.group(1) if title_match else "Unknown Title"
    return title

def extract_example(html_content):
    match = re.search(r'<pre><code>(.*?)</code></pre>', html_content, re.DOTALL)
    if match:
        return html.unescape(match.group(1))
    return None

def generate_java_content(day_num, title, input_type):
    day_xx = f"Day{day_num:02d}"
    day_x = str(day_num)

    imports = ["org.bakh.adventofcode.Day"]
    type_str = "List<String>"
    constructor_super = "super(fileName);"

    if input_type == 1: # List<String>
        imports.append("java.util.List")
        type_str = "List<String>"
        constructor_super = "super(fileName);"

    elif input_type == 2: # Grid
        imports.append("org.bakh.adventofcode.utils.data.Grid")
        imports.append("org.bakh.adventofcode.utils.ParserUtils")
        type_str = "Grid"
        constructor_super = "super(fileName, ParserUtils.SPATIAL_MATRIX);"

    elif input_type == 3: # List<Point>
        imports.append("java.util.List")
        imports.append("org.bakh.adventofcode.utils.data.Point")
        imports.append("org.bakh.adventofcode.utils.ParserUtils")
        type_str = "List<Point>"
        constructor_super = "super(fileName, ParserUtils.POINTS);"

    elif input_type == 4: # Set<Point>
        imports.append("java.util.Set")
        imports.append("org.bakh.adventofcode.utils.data.Point")
        imports.append("org.bakh.adventofcode.utils.ParserUtils")
        type_str = "Set<Point>"
        constructor_super = "super(fileName, ParserUtils.POINTS_SET);"

    # Construct import block
    import_block = ""
    for imp in imports:
        import_block += f"import {imp};\n"

    template = f"""package org.bakh.adventofcode.aoc{YEAR};

{import_block}

/**
 * <a href=\"https://adventofcode.com/{YEAR}/day/{day_x}\">Day {day_x}: {title}</a>
 */
public class {day_xx} extends Day<{type_str}> {{

    public {day_xx}(final String fileName) {{
        {constructor_super}
    }}

    static void main() {{
        new {day_xx}("{YEAR}/day{day_num:02d}.input").printParts();
    }}

    @Override
    public String runPartOne() {{
        return "";
    }}

    @Override
    public String runPartTwo() {{
        return "";
    }}

}} """
    return template

def update_parameterized_tests(day_num):
    test_file_name = f"Days{YEAR}ParameterizedTests.java"
    test_file_path = os.path.join(JAVA_TEST_PATH, f"aoc{YEAR}", test_file_name)

    if not os.path.exists(test_file_path):
        print(f"Warning: Test file {test_file_path} not found.")
        return

    with open(test_file_path, "r") as f:
        content = f.read()

    day_class_ref = f"Day{day_num:02d}.class"

    if day_class_ref in content:
        print(f"Test entry for Day {day_num} already exists.")
        return

    new_entry = f'            Arguments.of(Named.of("Day {day_num:02d}", {day_class_ref}), "{YEAR}/day{day_num:02d}.input", "", "")'

    pattern = r'(Arguments\.of\(.*?\))(\s*)\);'

    matches = list(re.finditer(pattern, content, re.DOTALL))
    if not matches:
        print("Could not find insertion point in test file.")
        return

    last_match = matches[-1]

    prev_arg = last_match.group(1)
    whitespace = last_match.group(2)

    replacement = f"{prev_arg},\n{new_entry}{whitespace});"

    new_content = content[:last_match.start()] + replacement + content[last_match.end():]

    with open(test_file_path, "w") as f:
        f.write(new_content)

    print(f"Updated {test_file_name} with new test case.")

def main():
    if len(sys.argv) > 1:
        day_num = int(sys.argv[1])
    else:
        now = datetime.datetime.now()
        if now.month == 12:
            day_num = now.day
        else:
            print("It's not December! Please specify a day.")
            sys.exit(1)

    print(f"--- Setting up Day {day_num} for Year {YEAR} ---")

    cookie = get_session_cookie()

    print("Fetching puzzle description...")
    puzzle_url = f"{BASE_URL}/{YEAR}/day/{day_num}"
    puzzle_html = fetch_url(puzzle_url, cookie)
    title = parse_title(puzzle_html)

    print(f"Title: {title}")

    example_content = extract_example(puzzle_html)
    if example_content:
        example_filename = f"day{day_num:02d}.input"
        example_path = os.path.join(RESOURCE_TEST_PATH, str(YEAR), example_filename)
        os.makedirs(os.path.dirname(example_path), exist_ok=True)
        with open(example_path, "w") as f:
            f.write(example_content)
        print(f"Saved example to {example_path}")
    else:
        print("No example code block found in puzzle description.")

    print("Fetching input data...")
    input_url = f"{BASE_URL}/{YEAR}/day/{day_num}/input"
    input_data = fetch_url(input_url, cookie)

    input_filename = f"day{day_num:02d}.input"
    input_path = os.path.join(RESOURCE_BASE_PATH, str(YEAR), input_filename)
    os.makedirs(os.path.dirname(input_path), exist_ok=True)

    with open(input_path, "w") as f:
        f.write(input_data)
    print(f"Saved input to {input_path}")

    print("\nSelect Input Type:")
    print("[1] List<String> (default)")
    print("[2] Grid")
    print("[3] List<Point>")
    print("[4] Set<Point>")

    choice = input("Choice [1]: ").strip()
    if choice == "2":
        type_choice = 2
    elif choice == "3":
        type_choice = 3
    elif choice == "4":
        type_choice = 4
    else:
        type_choice = 1

    java_filename = f"Day{day_num:02d}.java"
    java_package_dir = os.path.join(JAVA_BASE_PATH, f"aoc{YEAR}")
    java_path = os.path.join(java_package_dir, java_filename)
    os.makedirs(java_package_dir, exist_ok=True)

    if os.path.exists(java_path):
        overwrite = input(f"{java_filename} already exists. Overwrite? (y/n) [n]: ").strip().lower()
        if overwrite not in ["y", "yes"]:
            print("Skipping Java file generation.")
        else:
             content = generate_java_content(day_num, title, type_choice)
             with open(java_path, "w") as f:
                 f.write(content)
             print(f"Generated {java_path}")
    else:
        content = generate_java_content(day_num, title, type_choice)
        with open(java_path, "w") as f:
            f.write(content)
        print(f"Generated {java_path}")

    update_parameterized_tests(day_num)

    print("\nDone!")

if __name__ == "__main__":
    main()
