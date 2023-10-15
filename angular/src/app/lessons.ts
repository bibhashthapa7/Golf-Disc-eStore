/**
 * @Purpose This is used to create a lesson object in typescript
 * @author Coolname
 */
export interface Lesson {
    id: number;
    username: string | null;
    title: string;
    description: string;
    days: string;
    startDate: string;
    endDate: string;
    price: number;
}