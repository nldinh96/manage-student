# Student Management System - DDD Architecture

## Tổng quan
Dự án Student Management được thiết kế theo kiến trúc **Domain-Driven Design (DDD)** với các layer rõ ràng và tách biệt.

## Kiến trúc DDD

### 1. Domain Layer (Tầng Miền)
**Package**: `com.example.student_management.domain`

Đây là **trung tâm của ứng dụng**, chứa toàn bộ business logic và các quy tắc nghiệp vụ.

#### 1.1. Model (`domain.model`)
- **Aggregate Root**: `Student` - Thực thể chính quản lý vòng đời của aggregate
- **Value Objects**: 
  - `StudentCode` - Mã sinh viên (immutable)
  - `FullName` - Họ và tên (immutable)
  - `Address` - Địa chỉ (immutable)
- **Enum**: `StudentStatus` - Trạng thái sinh viên

#### 1.2. Repository Interfaces (`domain.repository`)
- `StudentRepository` - Interface định nghĩa các phương thức truy xuất dữ liệu (không chứa implementation)

#### 1.3. Domain Services (`domain.service`)
- `StudentDomainService` - Xử lý logic nghiệp vụ không thuộc về một entity cụ thể (validation, generation)

### 2. Application Layer (Tầng Ứng dụng)
**Package**: `com.example.student_management.application`

Điều phối các use cases và tương tác giữa các layer.

#### 2.1. Services (`application.service`)
- `StudentApplicationService` - Orchestrate các use cases như tạo, cập nhật, xóa sinh viên

#### 2.2. DTOs (`application.dto`)
- `StudentDTO` - Data transfer object cho response
- `CreateStudentRequest` - Request DTO cho việc tạo sinh viên
- `UpdateStudentRequest` - Request DTO cho việc cập nhật sinh viên

#### 2.3. Mappers (`application.mapper`)
- `StudentMapper` - Chuyển đổi giữa Domain entities và DTOs

### 3. Infrastructure Layer (Tầng Hạ tầng)
**Package**: `com.example.student_management.infrastructure`

Cung cấp implementation cho các technical concerns.

#### 3.1. Persistence (`infrastructure.persistence`)
- `JpaStudentRepository` - Spring Data JPA repository interface
- `StudentRepositoryImpl` - Implementation của domain repository interface

#### 3.2. Configuration (`infrastructure.config`)
- `DomainConfig` - Configuration cho domain layer

#### 3.3. Exception Handling (`infrastructure.exception`)
- `GlobalExceptionHandler` - Xử lý exception toàn cục
- `ErrorResponse`, `ValidationErrorResponse` - Response structures

### 4. Presentation Layer (Tầng Giao diện)
**Package**: `com.example.student_management.presentation`

Expose APIs cho client.

#### 4.1. REST API (`presentation.rest`)
- `StudentController` - RESTful endpoints

#### 4.2. GraphQL API (`presentation.graphql`)
- `StudentGraphQLController` - GraphQL queries và mutations

## Cấu trúc thư mục

```
src/main/java/com/example/student_management/
├── domain/                          # Domain Layer (Core Business Logic)
│   ├── model/
│   │   ├── shared/
│   │   │   └── AggregateRoot.java
│   │   └── student/
│   │       ├── Student.java          # Aggregate Root
│   │       ├── StudentCode.java      # Value Object
│   │       ├── FullName.java         # Value Object
│   │       ├── Address.java          # Value Object
│   │       └── StudentStatus.java    # Enum
│   ├── repository/
│   │   └── StudentRepository.java    # Repository Interface
│   └── service/
│       └── StudentDomainService.java # Domain Service
│
├── application/                     # Application Layer (Use Cases)
│   ├── dto/
│   │   ├── StudentDTO.java
│   │   ├── CreateStudentRequest.java
│   │   └── UpdateStudentRequest.java
│   ├── mapper/
│   │   └── StudentMapper.java
│   └── service/
│       └── StudentApplicationService.java
│
├── infrastructure/                  # Infrastructure Layer (Technical)
│   ├── persistence/
│   │   ├── JpaStudentRepository.java
│   │   └── StudentRepositoryImpl.java
│   ├── config/
│   │   └── DomainConfig.java
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       ├── ErrorResponse.java
│       └── ValidationErrorResponse.java
│
└── presentation/                    # Presentation Layer (APIs)
    ├── rest/
    │   └── StudentController.java
    └── graphql/
        └── StudentGraphQLController.java
```

## Nguyên tắc DDD được áp dụng

### 1. **Ubiquitous Language**
- Sử dụng ngôn ngữ nghiệp vụ nhất quán: Student, Enrollment, Status, etc.

### 2. **Aggregates & Aggregate Roots**
- `Student` là Aggregate Root quản lý toàn bộ thông tin sinh viên
- Đảm bảo tính nhất quán của dữ liệu trong aggregate

### 3. **Value Objects**
- `StudentCode`, `FullName`, `Address` là immutable
- Chứa validation logic và business rules

### 4. **Repository Pattern**
- Domain layer định nghĩa interface
- Infrastructure layer cung cấp implementation
- Che giấu chi tiết persistence

### 5. **Domain Services**
- Logic nghiệp vụ không thuộc về một entity cụ thể
- Validation, generation logic

### 6. **Separation of Concerns**
- Mỗi layer có trách nhiệm riêng biệt
- Domain layer độc lập với infrastructure

## API Endpoints

### GraphQL API
- Truy cập GraphiQL tại: `http://localhost:8081/graphiql`
- Endpoint: `http://localhost:8081/graphql`

---

### Queries

#### 1. Lấy thông tin sinh viên theo ID

**Query:**
```graphql
query GetStudent($id: ID!) {
  student(id: $id) {
    id
    studentCode
    firstName
    lastName
    email
    dateOfBirth
    status
    street
    city
    state
    postalCode
    country
    createdAt
    updatedAt
  }
}
```

**Variables:**
```json
{
  "id": 1
}
```

---

#### 2. Lấy danh sách tất cả sinh viên

**Query:**
```graphql
query GetAllStudents {
  students {
    id
    studentCode
    firstName
    lastName
    email
    status
  }
}
```

**Variables:**
```json
{}
```

---

#### 3. Lấy sinh viên theo mã sinh viên

**Query:**
```graphql
query GetStudentByCode($code: String!) {
  studentByCode(code: $code) {
    id
    studentCode
    firstName
    lastName
    email
    status
  }
}
```

**Variables:**
```json
{
  "code": "ST123456"
}
```

---

#### 4. Lấy danh sách sinh viên theo trạng thái

**Query:**
```graphql
query GetStudentsByStatus($status: StudentStatus!) {
  studentsByStatus(status: $status) {
    id
    studentCode
    firstName
    lastName
    email
    status
  }
}
```

**Variables:**
```json
{
  "status": "ACTIVE"
}
```

---

### Mutations

#### 1. Tạo sinh viên mới

**Query:**
```graphql
mutation CreateStudent($input: CreateStudentInput!) {
  createStudent(input: $input) {
    id
    studentCode
    firstName
    lastName
    email
    status
    createdAt
  }
}
```

**Variables:**
```json
{
  "input": {
    "studentCode": "ST123456",
    "firstName": "Nguyen",
    "lastName": "Van A",
    "email": "nguyenvana@example.com",
    "dateOfBirth": "2000-01-15",
    "street": "123 Nguyen Hue",
    "city": "Ho Chi Minh",
    "state": "HCM",
    "postalCode": "700000",
    "country": "Vietnam"
  }
}
```

---

#### 2. Cập nhật thông tin sinh viên

**Query:**
```graphql
mutation UpdateStudent($id: ID!, $input: UpdateStudentInput!) {
  updateStudent(id: $id, input: $input) {
    id
    studentCode
    firstName
    lastName
    email
    status
    updatedAt
  }
}
```

**Variables:**
```json
{
  "id": 1,
  "input": {
    "firstName": "Nguyen Updated",
    "lastName": "Van B",
    "email": "nguyenvanb@example.com"
  }
}
```

---

#### 3. Kích hoạt sinh viên

**Query:**
```graphql
mutation ActivateStudent($id: ID!) {
  activateStudent(id: $id) {
    id
    studentCode
    status
  }
}
```

**Variables:**
```json
{
  "id": 1
}
```

---

#### 4. Đình chỉ sinh viên

**Query:**
```graphql
mutation SuspendStudent($id: ID!) {
  suspendStudent(id: $id) {
    id
    studentCode
    status
  }
}
```

**Variables:**
```json
{
  "id": 1
}
```

---

#### 5. Tốt nghiệp sinh viên

**Query:**
```graphql
mutation GraduateStudent($id: ID!) {
  graduateStudent(id: $id) {
    id
    studentCode
    status
  }
}
```

**Variables:**
```json
{
  "id": 1
}
```

---

#### 6. Xóa sinh viên

**Query:**
```graphql
mutation DeleteStudent($id: ID!) {
  deleteStudent(id: $id)
}
```

**Variables:**
```json
{
  "id": 1
}
```

---

## Công nghệ sử dụng

- **Spring Boot 4.0.0** - Framework chính
- **Spring Data JPA** - ORM và persistence
- **MySQL** - Database
- **Redis** - Caching (có thể mở rộng)
- **Spring GraphQL** - GraphQL API
- **Lombok** - Giảm boilerplate code
- **Jakarta Validation** - Input validation

## Cách chạy dự án

### 1. Chuẩn bị Database
Đảm bảo MySQL đang chạy trên localhost:3306 với:
- Database: `student_management` (sẽ tự động tạo)
- Username: `root`
- Password: `123456789`

### 2. Build và Run
```bash
# Windows
.\gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

### 3. Test GraphQL API
Mở trình duyệt và truy cập: `http://localhost:8081/graphiql`

## Lợi ích của kiến trúc DDD

1. **Maintainability**: Dễ bảo trì với sự tách biệt rõ ràng
2. **Testability**: Dễ dàng test từng layer độc lập
3. **Scalability**: Dễ mở rộng và thêm tính năng mới
4. **Business-Focused**: Domain layer phản ánh đúng nghiệp vụ
5. **Flexibility**: Dễ thay đổi technical implementation mà không ảnh hưởng domain logic

## Mở rộng trong tương lai

- [ ] Thêm Course aggregate
- [ ] Thêm Enrollment aggregate để quản lý đăng ký môn học
- [ ] Implement caching với Redis
- [ ] Thêm Domain Events
- [ ] Implement CQRS pattern
- [ ] Thêm integration tests
- [ ] Thêm API documentation (Swagger/OpenAPI)
