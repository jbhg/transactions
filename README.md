- A job is represented by a series of date-stamped "change" objects
- Changes can have one of 5 types:
  - "CREATE"  -- indicates when job was created
  - "HIRE"    -- person starts job, job goes from open => filled
  - "UPDATE"  -- updates the job's attributes (eg. manager, compensation, custom fields)
  - "DEPART"  -- person departs job, job goes from filled => open
  - "DELETE"  -- job deleted from system

- Only changes with an ACTIVE status should be considered
- Changes are not stored in order
- A person is represented separately in a canonical object with no time series component

==============================

1. What is the base salary of the job with ID "5a13d80dcfed7957fe6c04a5" on May 5th, 2019?

2. What does Samson Oren's job look like on April 30th, 2019?

3. How many open jobs exist on March 4th, 2018?

4. How many people report up to Samson Oren on June 15th, 2018?