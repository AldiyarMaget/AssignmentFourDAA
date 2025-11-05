import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("algorithm_comparison.csv")

print("=== Data Table ===")
print(df)

plt.figure(figsize=(12, 6))
plt.plot(df['Dataset'], df['SCC Time (ms)'], marker='o', label='SCC Time (ms)')
plt.plot(df['Dataset'], df['Topo Time (ms)'], marker='o', label='Topo Time (ms)')
plt.plot(df['Dataset'], df['DAG-SP Time (ms)'], marker='o', label='DAG-SP Time (ms)')

plt.title('Execution Times Comparison')
plt.xlabel('Dataset')
plt.ylabel('Time (ms)')
plt.xticks(rotation=45)
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.show()

df.plot(x='Dataset', y=['SCC Visits', 'DAG Relaxations'], kind='bar', figsize=(12, 6))
plt.title('SCC Visits vs DAG Relaxations')
plt.ylabel('Count')
plt.xticks(rotation=45)
plt.grid(True)
plt.tight_layout()
plt.show()

df.plot(x='Dataset', y=['SCC Count', 'SCC Edges'], kind='bar', figsize=(12, 6))
plt.title('SCC Count vs SCC Edges')
plt.ylabel('Count')
plt.xticks(rotation=45)
plt.grid(True)
plt.tight_layout()
plt.show()
