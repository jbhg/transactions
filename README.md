**API Endpoints:**

    GET /api/jobs/{id} -- returns job details, without respect to assignment.

    GET /api/jobAssignments/{job_id}?{depth}
        -- returns job details including assignment (if available)

    GET /api/jobAssignments/{job_id}/reports
        -- returns job details including assignment and direct reporting jobs.

    GET /api/jobAssignments/{job_id}/reports/{depth}
        -- returns job details including assignment and reporting jobs at recursive depth.

    PUT /api/jobAssignments/{job_id}/people/{people_id}
        -- assign people_id entry to job with job_id.

    POST /api/jobs
    PUT /api/jobs/{id}
    DELETE /api/jobs/{job_id}/people/{people_id}
        -- remove person association from job, leaving it vacant.

    GET /api/people/{id}
    POST /api/people
    PUT /api/people/{id}

**Models:**

OrgChartAPIController.java

v

OrgChartAPIService.java

    JobAssignment getJobAssignment(int jobId, int depth) {
        final Job job = jobRepository.getById(jobId); // TODO: populate details of associated person if present

        // final List<JobAssignment> directReports = jobRepository.getByReportsTo(jobId).stream().map(job -> new JobAssignment(job)).collectors.toList();

        final List<JobAssignment> directReports = depth > 0 ?
            jobRepository.getByReportsTo(jobId).stream().map(childJob -> getJobAssignment(childJob.id, depth - 1)).collectors.toList() :
            ImmutableList.empty();

        return new JobAssignment(job, directReports);

        // look up job and its associated person; assign to job.
        // then, look up all jobs for which job is the direct parent; populate these (with associated person) to directReports.
        // populate each JobAssignment in directReports "recursively" until depth is exhausted.
    }
    
v

OrgChartManager.java implements IOrgChartManager

JobAssignment.java
    - Job job (which includes the assigned person)
    - Collection<JobAssignment> directReports 
    

Job.java:

    [INDEXED] id int unique autoincrement
    department varchar (but in the future this might be a fk to a row in a table)
    job_title varchar
    [INDEXED] reports_to int references another job (optional)
    [INDEXED] people_id int references person who holds this job (optional -- might be vacant)
        Optional<Person> instead of Person (nullable)

JobRepository.java (somewhat delegates to Spring's builtin JDBC framework, also uses PersonRepository for some calls)

    Will need to include some tranactionally-safe calls to the Spring Repository code.

Person.java:

    [INDEXED] id int unique autoincrement
    firstname varchar
    lastname varchar
    startdate date

PersonRepository.java (mostly delegates to Spring's builtin JDBC framework)


**Procedures**

_Assign person to new job_
    
    Job assignPersonToJob(int personId, int jobId) throws IllegalJobAssignmentException;

   - If job is not vacant, fail because we don't know what to do with the person already in that job.
   - If person has a job assignment already, make it vacant.
   - Then, assign person to job.

3. 