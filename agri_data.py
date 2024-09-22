import csv
from pymysql import Connection

conn = Connection(
    host="47.120.63.251",   # 主机名（IP）
    port=3306,          # 端口
    user="root",        # 账户
    password="123",  # 密码
    autocommit=True     # 设置自动提交
)


# 创建游标对象
cursor = conn.cursor()

# 选择数据库
conn.select_db("agri_predictor")

cursor.execute("DROP TABLE IF EXISTS agri_data")

sql = """CREATE TABLE agri_data (
         agri_name  CHAR(20),
         suitable_low  CHAR(20),
         suitable_high CHAR(20),  
         max_low CHAR(20),
         max_high CHAR(20) )"""

cursor.execute(sql)


sql = """
       INSERT INTO agri_data (agri_name, suitable_low, suitable_high, max_low, max_high)
       VALUES(%s, %s, %s, %s, %s)
       """


with open('agritem.csv','r',encoding="GBK") as f :
    reader = csv.reader(f)
    a = 0
    for row in reader:
        a += 1
        if a == 1:
            continue
        i = tuple(row)

        cursor.execute(sql, i)
        conn.commit()


sql = "SELECT * FROM agri_data"
cursor.execute(sql)
# 只要不涉及数据的更改，可以不需要调用commit()方法提交更改
result = cursor.fetchall()
for row in result:
    print(row)



# 关闭游标和连接
cursor.close()
conn.close()